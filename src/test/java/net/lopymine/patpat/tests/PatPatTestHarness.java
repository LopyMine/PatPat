package net.lopymine.patpat.tests;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class PatPatTestHarness implements AutoCloseable {

	public static final String SERVER_HOST = "127.0.0.1";
	public static final int SERVER_PORT_BASE = 25599;
	public static final String WORLD_NAME = "patpat_test_world";
	public static final String CLIENT_TWO_NAME = "nikita51";
	public static final String PORT_FILE_NAME = "patpat-test-port.txt";

	private static final long CONNECT_TIMEOUT_SECONDS = 600L;
	private static final long COMMAND_TIMEOUT_SECONDS = 300L;

	private final Path root;
	private final String project;
	private final String loader;
	private final String version;
	private final ServerSocket serverSocket;
	private final List<Process> processes = new ArrayList<>();
	private final Map<String, Agent> agents = new ConcurrentHashMap<>();
	private final CountDownLatch expectedAgents = new CountDownLatch(3);
	private final FileChannel lockChannel;
	private final FileLock lock;
	private final int lane;
	private Thread shutdownHook;

	public PatPatTestHarness(Path root, String project) throws IOException, InterruptedException {
		this.root    = root;
		this.project = project;
		this.loader  = project.substring(0, project.indexOf('-'));
		this.version = project.substring(project.indexOf('-') + 1);

		Files.createDirectories(root.resolve("build/patpat-tests"));

		int lanes = readParallelRuns(root);
		FileLock acquired = null;
		FileChannel channel = null;
		int acquiredLane = -1;

		while (acquired == null) {
			for (int candidate = 1; candidate <= lanes; candidate++) {
				channel = FileChannel.open(root.resolve("build/patpat-tests/.lane-%d.lock".formatted(candidate)),
						StandardOpenOption.CREATE, StandardOpenOption.WRITE);
				acquired = channel.tryLock();
				if (acquired != null) {
					acquiredLane = candidate;
					break;
				}
				channel.close();
			}
			if (acquired == null) {
				Thread.sleep(1000L);
			}
		}

		this.lockChannel = channel;
		this.lock        = acquired;
		this.lane        = acquiredLane;

		this.serverSocket = new ServerSocket(0, 16, InetAddress.getByName(SERVER_HOST));
		this.log("Acquired lane %d of %d (minecraft port %d)", this.lane, lanes, this.getServerPort());

		Thread acceptor = new Thread(this::acceptLoop, "patpat-test-harness-acceptor");
		acceptor.setDaemon(true);
		acceptor.start();

		this.shutdownHook = new Thread(this::forceCleanup, "patpat-test-harness-shutdown");
		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
	}

	private void forceCleanup() {
		for (Process process : this.processes) {
			destroyTree(process.toHandle());
		}
		try {
			deleteLanePortFiles();
		} catch (IOException ignored) {
			// nothing to do
		}
	}

	public String getLoader() {
		return this.loader;
	}

	public String getVersion() {
		return this.version;
	}

	public int getServerPort() {
		return SERVER_PORT_BASE + this.lane;
	}

	private Path laneDir(String name) {
		return this.root.resolve("runs/tests/%d_%s".formatted(this.lane, name));
	}

	private void deleteLanePortFiles() throws IOException {
		for (String name : List.of("server", "client", "client_" + CLIENT_TWO_NAME)) {
			Files.deleteIfExists(this.laneDir(name).resolve(PORT_FILE_NAME));
		}
	}

	private static int readParallelRuns(Path root) throws IOException {
		Properties properties = new Properties();
		try (InputStream stream = Files.newInputStream(root.resolve("gradle.properties"))) {
			properties.load(stream);
		}
		try {
			return Math.max(1, Integer.parseInt(properties.getProperty("tests.parallel_runs", "1").trim()));
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	public void log(String message, Object... arguments) {
		System.out.printf("[%s] [%s] %s%n", LocalTime.now().truncatedTo(ChronoUnit.MILLIS), this.project, message.formatted(arguments));
	}

	public int getPort() {
		return this.serverSocket.getLocalPort();
	}

	public void awaitServerAcceptingConnections() throws InterruptedException {
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(CONNECT_TIMEOUT_SECONDS);
		while (System.nanoTime() < deadline) {
			try (Socket probe = new Socket()) {
				probe.connect(new InetSocketAddress(SERVER_HOST, this.getServerPort()), 1000);
				this.log("Minecraft server is accepting connections on %s:%d", SERVER_HOST, this.getServerPort());
				return;
			} catch (IOException e) {
				this.assertProcessesAlive();
				Thread.sleep(500L);
			}
		}
		throw new AssertionError("Minecraft server did not start listening on %s:%d".formatted(SERVER_HOST, this.getServerPort()));
	}

	public Path collectScreenshotWithCustomName(String sourcePath, String name) throws IOException {
		Path target = this.root.resolve("runs-tests").resolve(name);
		Files.createDirectories(target.getParent());
		Files.copy(Path.of(sourcePath), target, StandardCopyOption.REPLACE_EXISTING);
		return target;
	}

	public Path collectScreenshot(String sourcePath, String player) throws IOException {
		Path target = this.root.resolve("runs-tests").resolve("%s+%s+%s.png".formatted(player, this.version, this.loader));
		Files.createDirectories(target.getParent());
		Files.copy(Path.of(sourcePath), target, StandardCopyOption.REPLACE_EXISTING);
		return target;
	}

	public void prepareServerDirectory() throws IOException {
		for (String name : List.of("server", "client", "client_" + CLIENT_TWO_NAME)) {
			Path dir = this.laneDir(name);
			Files.createDirectories(dir);
			deleteRecursively(dir.resolve("config"));
			Files.writeString(dir.resolve(PORT_FILE_NAME), String.valueOf(this.getPort()));
		}

		Path serverDir = this.laneDir("server");
		Files.writeString(serverDir.resolve("eula.txt"), "eula=true\n");

		String properties = String.join("\n",
				"online-mode=false",
				"server-ip=" + SERVER_HOST,
				"server-port=" + this.getServerPort(),
				"level-name=" + WORLD_NAME,
				"level-type=flat",
				"generate-structures=false",
				"level-seed=patpat",
				"gamemode=creative",
				"force-gamemode=true",
				"difficulty=peaceful",
				"spawn-monsters=false",
				"spawn-animals=false",
				"spawn-npcs=false",
				"allow-flight=true",
				"spawn-protection=0",
				"max-players=4",
				"view-distance=8",
				"simulation-distance=8",
				"sync-chunk-writes=false",
				"enable-status=false",
				"motd=PatPat Test"
		);
		Files.writeString(serverDir.resolve("server.properties"), properties + "\n");

		deleteRecursively(serverDir.resolve(WORLD_NAME));
	}

	public void prepareSingleplayerDirectory() throws IOException {
		Path dir = this.laneDir("client");
		Files.createDirectories(dir);
		deleteRecursively(dir.resolve("config"));
		deleteRecursively(dir.resolve("mods"));
		deleteRecursively(dir.resolve("shaderpacks"));
		deleteRecursively(dir.resolve("saves").resolve(WORLD_NAME));
		Files.writeString(dir.resolve(PORT_FILE_NAME), String.valueOf(this.getPort()));
	}

	private Path suiteDir(String suite) {
		return this.root.resolve("versions").resolve(this.project).resolve("tests").resolve(suite);
	}

	public boolean hasSuiteMods(String suite) {
		Path mods = this.suiteDir(suite).resolve("mods");
		if (!Files.isDirectory(mods)) {
			return false;
		}
		try (Stream<Path> stream = Files.list(mods)) {
			return stream.anyMatch(path -> path.getFileName().toString().endsWith(".jar"));
		} catch (IOException e) {
			return false;
		}
	}

	public void installSuiteAssets(String suite) throws IOException {
		Path source = this.suiteDir(suite);
		if (!Files.isDirectory(source)) {
			return;
		}
		Path dest = this.laneDir("client");
		try (Stream<Path> stream = Files.list(source)) {
			List<Path> directories = stream.filter(Files::isDirectory).toList();
			for (Path directory : directories) {
				Path target = dest.resolve(directory.getFileName().toString());
				deleteRecursively(target);
				copyRecursively(directory, target);
			}
		}
		this.log("Installed suite '%s' assets into %s", suite, dest);
	}

	private static void copyRecursively(Path source, Path target) throws IOException {
		try (Stream<Path> stream = Files.walk(source)) {
			List<Path> entries = stream.toList();
			for (Path entry : entries) {
				Path resolved = target.resolve(source.relativize(entry).toString());
				if (Files.isDirectory(entry)) {
					Files.createDirectories(resolved);
				} else {
					Files.createDirectories(resolved.getParent());
					Files.copy(entry, resolved, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	public void assertServerPortIsFree() {
		try (Socket probe = new Socket()) {
			probe.connect(new InetSocketAddress(SERVER_HOST, this.getServerPort()), 1000);
		} catch (IOException e) {
			return;
		}
		throw new AssertionError("Port %d is already in use, a previous test run is probably still alive".formatted(this.getServerPort()));
	}

	public Agent launchServer(String... arguments) throws IOException, InterruptedException {
		this.processes.add(this.launch("runServerTest", "server", arguments));
		return this.awaitAgent("server");
	}

	public Agent launchClientOne(String... arguments) throws IOException, InterruptedException {
		this.processes.add(this.launch("runClientTest", "client", arguments));
		return this.awaitAgent("client1");
	}

	public Agent launchClientTwo(String... arguments) throws IOException, InterruptedException {
		this.processes.add(this.launch("runClientTest_" + CLIENT_TWO_NAME, "client2", arguments));
		return this.awaitAgent("client2");
	}

	private Process launch(String task, String logName, String... arguments) throws IOException {
		String wrapper = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win") ? "gradlew.bat" : "gradlew";

		List<String> command = new ArrayList<>(List.of(
				this.root.resolve(wrapper).toString(),
				":%s:%s".formatted(this.project, task),
				"-Ppatpat.test.port=" + this.getPort(),
				"-Pmossy.lane=" + this.lane,
				"--configure-on-demand",
				"--console=plain"
		));

		command.addAll(List.of(arguments));

		Path logDir = this.root.resolve("build/patpat-tests");
		Files.createDirectories(logDir);

		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(this.root.toFile());
		builder.redirectErrorStream(true);
		builder.redirectOutput(logDir.resolve("%s-%s.log".formatted(this.project, logName)).toFile());

		this.log("Launching %s (log: %s-%s.log)", task, this.project, logName);
		return builder.start();
	}

	private void acceptLoop() {
		while (!this.serverSocket.isClosed()) {
			try {
				Socket socket = this.serverSocket.accept();
				socket.setTcpNoDelay(true);
				new Agent(socket);
			} catch (IOException e) {
				return;
			}
		}
	}

	public Agent awaitAgent(String key) throws InterruptedException {
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(CONNECT_TIMEOUT_SECONDS);
		while (System.nanoTime() < deadline) {
			Agent agent = this.agents.get(key);
			if (agent != null) {
				this.log("Agent '%s' is ready", key);
				return agent;
			}
			this.assertProcessesAlive();
			Thread.sleep(250L);
		}
		throw new AssertionError("Agent '%s' did not connect in time".formatted(key));
	}

	private void assertProcessesAlive() {
		for (Process process : this.processes) {
			if (!process.isAlive() && process.exitValue() != 0) {
				throw new AssertionError("A game process died with exit code %d, see build/patpat-tests/*.log".formatted(process.exitValue()));
			}
		}
	}

	private static void destroyTree(ProcessHandle handle) {
		handle.descendants().forEach(ProcessHandle::destroyForcibly);
		handle.destroyForcibly();
	}

	private static void deleteRecursively(Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		try (Stream<Path> stream = Files.walk(path)) {
			stream.sorted(Comparator.reverseOrder()).forEach(entry -> {
				try {
					Files.delete(entry);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
		}
	}

	@Override
	public void close() {
		try {
			Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
		} catch (IllegalStateException ignored) {
			// the JVM is already shutting down, the hook is running anyway
		}

		for (Agent agent : this.agents.values()) {
			try {
				agent.send("QUIT", "");
			} catch (Exception ignored) {
				// the process is going away anyway
			}
		}
		for (Process process : this.processes) {
			try {
				if (process.waitFor(20L, TimeUnit.SECONDS)) {
					continue;
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			destroyTree(process.toHandle());
		}
		for (Process process : this.processes) {
			try {
				process.waitFor(20L, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		try {
			deleteLanePortFiles();
		} catch (IOException ignored) {
			// nothing to do
		}
		try {
			this.lock.release();
			this.lockChannel.close();
		} catch (IOException ignored) {
			// nothing to do
		}
		try {
			this.serverSocket.close();
		} catch (IOException ignored) {
			// nothing to do
		}
	}

	public class Agent {

		private final Socket socket;
		private final BufferedWriter writer;
		private final Map<Integer, CompletableFuture<String>> pending = new ConcurrentHashMap<>();
		private String key = "?";
		private int nextId = 1;

		private Agent(Socket socket) throws IOException {
			this.socket = socket;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

			Thread reader = new Thread(this::readLoop, "patpat-test-harness-reader");
			reader.setDaemon(true);
			reader.start();
		}

		private void readLoop() {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.trim().split(" ", 3);
					if (parts.length < 2) {
						continue;
					}
					if ("HELLO".equals(parts[1])) {
						this.register(parts.length > 2 ? parts[2] : "");
						continue;
					}
					CompletableFuture<String> future = this.pending.remove(Integer.parseInt(parts[0]));
					if (future == null) {
						continue;
					}
					if ("OK".equals(parts[1])) {
						future.complete(parts.length > 2 ? parts[2] : "");
					} else {
						future.completeExceptionally(new AssertionError(parts.length > 2 ? parts[2] : "unknown error"));
					}
				}
			} catch (IOException ignored) {
				// connection closed
			}
			this.pending.values().forEach(future -> future.completeExceptionally(new AssertionError("Agent disconnected")));
		}

		private void register(String payload) {
			String[] parts = payload.trim().split(" ", 2);
			String role = parts[0];
			String name = parts.length > 1 ? parts[1] : "";

			if ("SERVER".equals(role)) {
				this.key = "server";
			} else {
				this.key = CLIENT_TWO_NAME.equals(name) ? "client2" : "client1";
			}
			PatPatTestHarness.this.log("Connected: %s (%s)", this.key, name);
			PatPatTestHarness.this.agents.put(this.key, this);
			PatPatTestHarness.this.expectedAgents.countDown();
		}

		public CompletableFuture<String> send(String command, String argument) {
			int id = this.nextId++;
			CompletableFuture<String> future = new CompletableFuture<>();
			this.pending.put(id, future);
			PatPatTestHarness.this.log("%s <- %s %s", this.key, command, argument);
			try {
				this.writer.write("%d %s %s".formatted(id, command, argument));
				this.writer.write('\n');
				this.writer.flush();
			} catch (IOException e) {
				future.completeExceptionally(e);
			}
			return future;
		}

		public String run(String command, String argument) throws Exception {
			long startedAt = System.nanoTime();
			try {
				String result = this.send(command, argument).get(COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
				PatPatTestHarness.this.log("%s -> %s done in %d ms%s", this.key, command, (System.nanoTime() - startedAt) / 1_000_000L, result.isBlank() ? "" : " (" + result + ")");
				return result;
			} catch (Exception e) {
				PatPatTestHarness.this.log("%s -> %s FAILED: %s", this.key, command, e.getMessage());
				throw e;
			}
		}
	}
}
