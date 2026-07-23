package net.lopymine.patpat.tests;

import java.nio.file.*;
import java.util.concurrent.*;
import net.lopymine.patpat.tests.PatPatTestHarness.Agent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("gameTest")
@Tag("twoPlayersGameTest")
public class PatPatTwoPlayersPatTest {

	private static final String SCREENSHOT_NAME = "two_players_pat";
	private static final long PACKET_TIMEOUT_SECONDS = 5L;

	@Test
	@Timeout(value = 30, unit = TimeUnit.MINUTES)
	void patBetweenTwoPlayersIsRenderedOnTheReceiver() throws Exception {
		Path projectDir = Path.of("").toAbsolutePath();
		Path root = projectDir.getParent().getParent();
		String project = projectDir.getFileName().toString();

		assertTrue(Files.exists(root.resolve("gradlew.bat")) || Files.exists(root.resolve("gradlew")), "Project root not found at " + root);

		try (PatPatTestHarness harness = new PatPatTestHarness(root, project)) {
			harness.assertServerPortIsFree();
			harness.prepareServerDirectory();

			Agent server = harness.launchServer();
			harness.awaitServerAcceptingConnections();

			Agent client1 = harness.launchClientOne();
			Agent client2 = harness.launchClientTwo();

			client1.run("PREPARE", "");
			client2.run("PREPARE", "");

			String address = "%s:%d".formatted(PatPatTestHarness.SERVER_HOST, harness.getServerPort());
			client1.run("JOIN", address);
			client2.run("JOIN", address);

			server.run("WAIT_PLAYERS", "2");

			for (String command : new String[]{
					"gamerule doDaylightCycle false",
					"gamerule doWeatherCycle false",
					"gamerule doMobSpawning false",
					"gamerule randomTickSpeed 0",
					"gamerule doFireTick false",
					"gamerule sendCommandFeedback false",
					"time set noon",
					"weather clear"
			}) {
				server.run("RUN_COMMAND", command);
			}

			String clientOneName = "LopyMine";
			client1.run("WAIT_TICKS", "20");
			client2.run("WAIT_TICKS", "20");

			server.run("RUN_COMMAND", "execute at %s run tp %s 0.5 ~ 0.5".formatted(clientOneName, clientOneName));
			server.run("RUN_COMMAND", "execute at %s run tp %s 2.5 ~ 0.5".formatted(PatPatTestHarness.CLIENT_TWO_NAME, PatPatTestHarness.CLIENT_TWO_NAME));

			client1.run("WAIT_TICKS", "20");
			client2.run("WAIT_TICKS", "20");

			client2.run("CAMERA", "THIRD_PERSON_BACK");
			client2.run("LOOK_AT", clientOneName);
			client1.run("LOOK_AT", PatPatTestHarness.CLIENT_TWO_NAME);

			CompletableFuture<String> patReceived = client2.send("WAIT_PAT", "SELF");

			client1.run("PAT", PatPatTestHarness.CLIENT_TWO_NAME);

			try {
				patReceived.get(PACKET_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				fail("Pat packet did not reach %s within %d seconds".formatted(PatPatTestHarness.CLIENT_TWO_NAME, PACKET_TIMEOUT_SECONDS));
			}

			client2.run("WAIT_TICKS", "5");

			Path receiver = harness.collectScreenshot(client2.run("SCREENSHOT", SCREENSHOT_NAME), "player_2");
			Path sender = harness.collectScreenshot(client1.run("SCREENSHOT", SCREENSHOT_NAME), "player_1");

			assertTrue(Files.size(receiver) > 0L, "Receiver screenshot is empty: " + receiver);
			assertTrue(Files.size(sender) > 0L, "Sender screenshot is empty: " + sender);
		}
	}
}
