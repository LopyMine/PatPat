package net.lopymine.patpat.tests;

import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import net.lopymine.patpat.tests.PatPatTestHarness.Agent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("gameTest")
@Tag("singleplayerGameTest")
public class PatPatSingleplayerPatTest {

	private static final String SCREENSHOT_NAME = "singleplayer_pat";
	private static final String COW_NAME = "Cow";

	@Test
	@Timeout(value = 30, unit = TimeUnit.MINUTES)
	void patOnEntityIsRenderedInSingleplayer() throws Exception {
		Path projectDir = Path.of("").toAbsolutePath();
		Path root = projectDir.getParent().getParent();
		String project = projectDir.getFileName().toString();

		assertTrue(Files.exists(root.resolve("gradlew.bat")) || Files.exists(root.resolve("gradlew")), "Project root not found at " + root);

		try (PatPatTestHarness harness = new PatPatTestHarness(root, project)) {
			harness.prepareSingleplayerDirectory();

			Agent client = harness.launchClientOne();

			client.run("PREPARE", "");
			client.run("CREATE_WORLD", PatPatTestHarness.WORLD_NAME);
			client.run("SETUP_WORLD", "");

			client.run("RUN_COMMAND", "execute at @p run tp @p ~ ~ ~ 0 0");
			client.run("RUN_COMMAND", "execute at @p run summon minecraft:cow ~ ~ ~2 {NoAI:1b}");

			client.run("WAIT_TICKS", "20");
			client.run("LOOK_AT", COW_NAME);
			client.run("PAT", COW_NAME);
			client.run("WAIT_TICKS", "5");

			Path screenshot = harness.collectScreenshot(client.run("SCREENSHOT", SCREENSHOT_NAME), "singleplayer");

			assertTrue(Files.size(screenshot) > 0L, "Screenshot is empty: " + screenshot);
		}
	}
}
