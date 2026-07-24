package net.lopymine.patpat.tests;

import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.lopymine.patpat.tests.PatPatTestHarness.Agent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("gameTest")
@Tag("shadersGameTest")
public class PatPatShadersPatTest {

	private static final String SUITE = "shaders";
	private static final String SCREENSHOT_PREFIX = "shaders_pat";
	private static final String COW_NAME = "Cow";
	private static final String[] SIDES = {"~ ~ ~3", "~3 ~ ~", "~ ~ ~-3", "~-3 ~ ~"};
	private static final Map<String, String> CLIENT_JVM_ARGS = Map.of("1.20.1", "-Dsodium.checks.issue2561=false");

	@Test
	@Timeout(value = 30, unit = TimeUnit.MINUTES)
	void patIsRenderedFromEverySideWithShaders() throws Exception {
		Path projectDir = Path.of("").toAbsolutePath();
		Path root = projectDir.getParent().getParent();
		String project = projectDir.getFileName().toString();

		assertTrue(Files.exists(root.resolve("gradlew.bat")) || Files.exists(root.resolve("gradlew")), "Project root not found at " + root);

		try (PatPatTestHarness harness = new PatPatTestHarness(root, project)) {
			if (!harness.hasSuiteMods(SUITE)) {
				harness.log("Skipping the shaders test, suite '%s' has no mods for this version", SUITE);
				return;
			}

			harness.prepareSingleplayerDirectory();
			harness.installSuiteAssets(SUITE);

			String jvmArgs = CLIENT_JVM_ARGS.getOrDefault(harness.getVersion(), "");
			Agent client = jvmArgs.isEmpty() ? harness.launchClientOne() : harness.launchClientOne("-Ppatpat.test.jvmargs=" + jvmArgs);

			client.run("PREPARE", "");
			client.run("CREATE_WORLD", PatPatTestHarness.WORLD_NAME);
			client.run("SETUP_WORLD", "");

			client.run("RUN_COMMAND", "execute at @p run tp @p ~ ~ ~ 0 0");
			client.run("RUN_COMMAND", "execute at @p run summon minecraft:cow ~ ~ ~2 {NoAI:1b}");
			client.run("WAIT_TICKS", "20");

			client.run("ENABLE_SHADERS", "");
			client.run("WAIT_TICKS", "20");

			client.run("LOOK_AT", COW_NAME);
			client.run("PAT", COW_NAME);
			client.run("WAIT_TICKS", "5");

			for (int side = 0; side < SIDES.length; side++) {
				client.run("RUN_COMMAND", "execute at @e[type=minecraft:cow,limit=1] run tp @p %s".formatted(SIDES[side]));
				client.run("WAIT_TICKS", "5");
				client.run("LOOK_AT", COW_NAME);
				client.run("WAIT_TICKS", "20");

				String name = "%s_%d".formatted(SCREENSHOT_PREFIX, side + 1);
				String screenshotName = "%s+%s+%s_%d.png".formatted(SCREENSHOT_PREFIX, harness.getVersion(), harness.getLoader(), side + 1);
				Path screenshot = harness.collectScreenshotWithCustomName(client.run("SCREENSHOT", name), screenshotName);
				assertTrue(Files.size(screenshot) > 0L, "Screenshot is empty: " + screenshot);
			}
		}
	}
}
