package net.lopymine.patpat.tests;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.*;
import net.lopymine.patpat.logger.PatLogger;

public class PatPatTestAgent {

	public static final String PORT_PROPERTY = "patpat.test.port";
	public static final String PORT_FILE = "patpat-test-port.txt";
	public static final PatLogger LOGGER = new PatLogger("PatPat/Test");

	private static final int PORT = resolvePort();

	private final ConcurrentLinkedQueue<PatPatTestRequest> requests = new ConcurrentLinkedQueue<>();
	private final String role;
	private Socket socket;
	private BufferedWriter writer;
	private boolean failed;
	@Setter
	private Runnable disconnectHandler;

	@Getter
	private boolean connected;

	public PatPatTestAgent(String role) {
		this.role = role;
	}

	public static boolean isEnabled() {
		return getPort() != -1;
	}

	public static int getPort() {
		return PORT;
	}

	private static int resolvePort() {
		int fromProperty = parsePort(System.getProperty(PORT_PROPERTY));
		if (fromProperty != -1) {
			return fromProperty;
		}

		Path file = Path.of("").toAbsolutePath().resolve(PORT_FILE);
		if (!Files.isRegularFile(file)) {
			return -1;
		}
		try {
			return parsePort(Files.readString(file));
		} catch (IOException e) {
			return -1;
		}
	}

	private static int parsePort(String value) {
		if (value == null) {
			return -1;
		}
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public void connect(String name) {
		if (this.connected || this.failed) {
			return;
		}
		int port = getPort();
		if (port == -1) {
			return;
		}
		try {
			this.socket = new Socket("127.0.0.1", port);
			this.socket.setTcpNoDelay(true);
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
			this.connected = true;

			Thread reader = new Thread(this::readLoop, "patpat-test-agent-reader");
			reader.setContextClassLoader(PatPatTestAgent.class.getClassLoader());
			reader.setDaemon(true);
			reader.start();

			this.send("0 HELLO %s %s".formatted(this.role, name));
			LOGGER.info("Test agent connected as {} ({})", this.role, name);
		} catch (IOException e) {
			this.failed = true;
			LOGGER.error("Failed to connect test agent to harness on port {}, giving up", port, e);
		}
	}

	private void readLoop() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				PatPatTestRequest request = PatPatTestRequest.parse(line);
				if (request != null) {
					this.requests.add(request);
				}
			}
		} catch (IOException e) {
			LOGGER.warn("Test agent connection closed: {}", e.getMessage());
		}

		this.connected = false;
		this.failed    = true;
		LOGGER.warn("Lost connection to the test harness, shutting down this game process");
		if (this.disconnectHandler != null) {
			this.disconnectHandler.run();
		}
	}

	public PatPatTestRequest poll() {
		return this.requests.poll();
	}

	public void ok(PatPatTestRequest request, String payload) {
		this.send("%d OK %s".formatted(request.id(), payload == null ? "" : payload));
	}

	public void error(PatPatTestRequest request, String message) {
		this.send("%d ERR %s".formatted(request.id(), message));
	}

	private synchronized void send(String line) {
		if (this.writer == null) {
			return;
		}
		try {
			this.writer.write(line);
			this.writer.write('\n');
			this.writer.flush();
		} catch (IOException e) {
			LOGGER.error("Failed to send line to harness", e);
		}
	}

	public record PatPatTestRequest(int id, String command, String argument) {

		public static PatPatTestRequest parse(String line) {
			String[] parts = line.trim().split(" ", 3);
			if (parts.length < 2) {
				return null;
			}
			try {
				return new PatPatTestRequest(Integer.parseInt(parts[0]), parts[1], parts.length > 2 ? parts[2] : "");
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
}
