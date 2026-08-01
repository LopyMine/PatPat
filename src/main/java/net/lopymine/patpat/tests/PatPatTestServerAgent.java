package net.lopymine.patpat.tests;

import java.util.concurrent.CompletableFuture;
import net.lopymine.patpat.entrypoint.*;
import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader.ModEnvironment;
import net.lopymine.patpat.tests.PatPatTestAgent.PatPatTestRequest;
import net.minecraft.server.MinecraftServer;

public class PatPatTestServerAgent {

	private static PatPatTestAgent agent;
	private static volatile MinecraftServer server;

	private PatPatTestServerAgent() {
		throw new IllegalStateException("Agent class");
	}

	public static void register() {
		if (!PatPatTestAgent.isEnabled()) {
			return;
		}
		if (EarlyCommonMultiLoader.getInstance().getEnvironment() != ModEnvironment.SERVER) {
			return;
		}
		agent = new PatPatTestAgent("SERVER");
		agent.setDisconnectHandler(() -> {
			MinecraftServer current = server;
			if (current != null) {
				current.halt(false);
			}
		});

		ServerMultiLoader.getInstance().registerServerPlayerLogListener((loggedIn, player) -> {
			if (server == null) {
				//? if >=1.20 {
				server = player.level().getServer();
				//?} else {
				/*server = player.level.getServer();
				*///?}
			}
		});

		Thread thread = new Thread(PatPatTestServerAgent::loop, "patpat-test-server-agent");
		thread.setContextClassLoader(PatPatTestServerAgent.class.getClassLoader());
		thread.setDaemon(true);
		thread.start();
	}

	private static void loop() {
		agent.connect("dedicated");

		while (true) {
			PatPatTestRequest request = agent.poll();
			if (request == null) {
				sleep();
				continue;
			}
			try {
				PatPatTestAgent.LOGGER.info("Starting command {} {}", request.command(), request.argument());
				handle(request);
				PatPatTestAgent.LOGGER.info("Finished command {}", request.command());
			} catch (Exception e) {
				PatPatTestAgent.LOGGER.error("Test command {} failed", request.command(), e);
				agent.error(request, e.toString());
			}
		}
	}

	private static void handle(PatPatTestRequest request) throws Exception {
		switch (request.command()) {
			case "WAIT_PLAYERS" -> {
				int expected = Integer.parseInt(request.argument().trim());
				while (server == null || server.getPlayerList().getPlayerCount() < expected) {
					sleep();
				}
				agent.ok(request, "");
			}
			case "RUN_COMMAND" -> {
				MinecraftServer current = awaitServer();
				CompletableFuture<Void> future = new CompletableFuture<>();
				current.execute(() -> {
					try {
						//? if >=1.19 {
						current.getCommands().performPrefixedCommand(current.createCommandSourceStack(), request.argument());
						//?} else {
						/*current.getCommands().performCommand(current.createCommandSourceStack(), request.argument());
						*///?}
						future.complete(null);
					} catch (Exception e) {
						future.completeExceptionally(e);
					}
				});
				future.get();
				agent.ok(request, "");
			}
			case "QUIT" -> {
				MinecraftServer current = awaitServer();
				current.halt(false);
				agent.ok(request, "");
			}
			default -> agent.error(request, "Unknown command: " + request.command());
		}
	}

	private static MinecraftServer awaitServer() {
		while (server == null) {
			sleep();
		}
		return server;
	}

	private static void sleep() {
		try {
			Thread.sleep(50L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
