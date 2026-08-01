package net.lopymine.patpat.tests;

import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import net.lopymine.patpat.tests.PatPatTestHarness.Agent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("gameTest")
@Tag("keybindingGameTest")
public class PatPatKeybindingChangeTest {

	private static final String COMBINATION = "CAPS_LOCK+T";
	private static final String EXPECTED_COMBINATION = "key.keyboard.caps.lock+key.keyboard.t";

	@Test
	@Timeout(value = 30, unit = TimeUnit.MINUTES)
	void keybindingIsReboundByRealInput() throws Exception {
		Path projectDir = Path.of("").toAbsolutePath();
		Path root = projectDir.getParent().getParent();
		String project = projectDir.getFileName().toString();

		assertTrue(Files.exists(root.resolve("gradlew.bat")) || Files.exists(root.resolve("gradlew")), "Project root not found at " + root);

		try (PatPatTestHarness harness = new PatPatTestHarness(root, project)) {
			harness.prepareSingleplayerDirectory();

			Agent client = harness.launchClientOne();

			client.run("PREPARE", "");
			client.run("OPEN_KEY_BINDS", "");
			client.run("SCROLL_TO_KEYBINDING", "");
			client.run("CLICK_KEYBINDING", "");
			client.run("WAIT_TICKS", "10");

			Path binding = harness.collectScreenshot(client.run("SCREENSHOT", "keybinding_1"), "keybinding_1");

			client.run("PRESS_KEYS", COMBINATION);
			client.run("WAIT_TICKS", "10");

			Path bound = harness.collectScreenshot(client.run("SCREENSHOT", "keybinding_2"), "keybinding_2");

			assertEquals(EXPECTED_COMBINATION, client.run("KEYBINDING", ""), "The PatPat keybinding was not rebound to " + COMBINATION);
			assertTrue(Files.size(binding) > 0L, "Binding screenshot is empty: " + binding);
			assertTrue(Files.size(bound) > 0L, "Bound screenshot is empty: " + bound);
		}
	}
}
