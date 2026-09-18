package net.lopymine.patpat.client.config.migrate;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.common.config.migrate.AbstractConfigMigrateHandler;

public class PatPatClientConfigMigrateVersion101 extends AbstractConfigMigrateHandler {

	private static final String MIGRATE_FILE_NAME = "patpat-client.json5";
	private static final String MIGRATE_VERSION = "1.0.1";
	private static final String UNKNOWN_KEY_NAME = "key.keyboard.unknown";
	private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Map<Integer, String> LEGACY_KEYSYM_NAMES = Map.ofEntries(
			Map.entry(48, "0"),
			Map.entry(49, "1"),
			Map.entry(50, "2"),
			Map.entry(51, "3"),
			Map.entry(52, "4"),
			Map.entry(53, "5"),
			Map.entry(54, "6"),
			Map.entry(55, "7"),
			Map.entry(56, "8"),
			Map.entry(57, "9"),
			Map.entry(65, "a"),
			Map.entry(66, "b"),
			Map.entry(67, "c"),
			Map.entry(68, "d"),
			Map.entry(69, "e"),
			Map.entry(70, "f"),
			Map.entry(71, "g"),
			Map.entry(72, "h"),
			Map.entry(73, "i"),
			Map.entry(74, "j"),
			Map.entry(75, "k"),
			Map.entry(76, "l"),
			Map.entry(77, "m"),
			Map.entry(78, "n"),
			Map.entry(79, "o"),
			Map.entry(80, "p"),
			Map.entry(81, "q"),
			Map.entry(82, "r"),
			Map.entry(83, "s"),
			Map.entry(84, "t"),
			Map.entry(85, "u"),
			Map.entry(86, "v"),
			Map.entry(87, "w"),
			Map.entry(88, "x"),
			Map.entry(89, "y"),
			Map.entry(90, "z"),
			Map.entry(290, "f1"),
			Map.entry(291, "f2"),
			Map.entry(292, "f3"),
			Map.entry(293, "f4"),
			Map.entry(294, "f5"),
			Map.entry(295, "f6"),
			Map.entry(296, "f7"),
			Map.entry(297, "f8"),
			Map.entry(298, "f9"),
			Map.entry(299, "f10"),
			Map.entry(300, "f11"),
			Map.entry(301, "f12"),
			Map.entry(302, "f13"),
			Map.entry(303, "f14"),
			Map.entry(304, "f15"),
			Map.entry(305, "f16"),
			Map.entry(306, "f17"),
			Map.entry(307, "f18"),
			Map.entry(308, "f19"),
			Map.entry(309, "f20"),
			Map.entry(310, "f21"),
			Map.entry(311, "f22"),
			Map.entry(312, "f23"),
			Map.entry(313, "f24"),
			Map.entry(314, "f25"),
			Map.entry(282, "num.lock"),
			Map.entry(320, "keypad.0"),
			Map.entry(321, "keypad.1"),
			Map.entry(322, "keypad.2"),
			Map.entry(323, "keypad.3"),
			Map.entry(324, "keypad.4"),
			Map.entry(325, "keypad.5"),
			Map.entry(326, "keypad.6"),
			Map.entry(327, "keypad.7"),
			Map.entry(328, "keypad.8"),
			Map.entry(329, "keypad.9"),
			Map.entry(334, "keypad.add"),
			Map.entry(330, "keypad.decimal"),
			Map.entry(335, "keypad.enter"),
			Map.entry(336, "keypad.equal"),
			Map.entry(332, "keypad.multiply"),
			Map.entry(331, "keypad.divide"),
			Map.entry(333, "keypad.subtract"),
			Map.entry(264, "down"),
			Map.entry(263, "left"),
			Map.entry(262, "right"),
			Map.entry(265, "up"),
			Map.entry(39, "apostrophe"),
			Map.entry(92, "backslash"),
			Map.entry(44, "comma"),
			Map.entry(61, "equal"),
			Map.entry(96, "grave.accent"),
			Map.entry(91, "left.bracket"),
			Map.entry(45, "minus"),
			Map.entry(46, "period"),
			Map.entry(93, "right.bracket"),
			Map.entry(59, "semicolon"),
			Map.entry(47, "slash"),
			Map.entry(32, "space"),
			Map.entry(258, "tab"),
			Map.entry(342, "left.alt"),
			Map.entry(341, "left.control"),
			Map.entry(340, "left.shift"),
			Map.entry(343, "left.win"),
			Map.entry(346, "right.alt"),
			Map.entry(345, "right.control"),
			Map.entry(344, "right.shift"),
			Map.entry(347, "right.win"),
			Map.entry(257, "enter"),
			Map.entry(256, "escape"),
			Map.entry(259, "backspace"),
			Map.entry(261, "delete"),
			Map.entry(269, "end"),
			Map.entry(268, "home"),
			Map.entry(260, "insert"),
			Map.entry(267, "page.down"),
			Map.entry(266, "page.up"),
			Map.entry(280, "caps.lock"),
			Map.entry(284, "pause"),
			Map.entry(281, "scroll.lock"),
			Map.entry(348, "menu"),
			Map.entry(283, "print.screen"),
			Map.entry(161, "world.1"),
			Map.entry(162, "world.2")
	);

	public PatPatClientConfigMigrateVersion101() {
		super(MIGRATE_FILE_NAME, MIGRATE_VERSION, PatPatClient.LOGGER);
	}

	@Override
	public boolean needToMigrateFile() {
		JsonObject object = this.readMigrateFile();
		if (object == null) {
			return false;
		}
		JsonObject combination = getPatCombination(object);
		if (combination == null) {
			return false;
		}
		return isLegacyKey(combination.get("attributeKey")) || isLegacyKey(combination.get("key"));
	}

	@Override
	public boolean migrateFile() {
		JsonObject object = this.readMigrateFile();
		if (object == null) {
			return false;
		}

		this.migrateFields(object);

		try (FileWriter writer = new FileWriter(this.getMigrateFile().toFile(), StandardCharsets.UTF_8)) {
			writer.write(PRETTY_GSON.toJson(object));
			return true;
		} catch (IOException e) {
			getLogger().error("[MigrateVersion101] Failed to save migrated file", e);
		}
		return false;
	}

	private JsonObject readMigrateFile() {
		try (JsonReader reader = new JsonReader(new FileReader(this.getMigrateFile().toFile(), StandardCharsets.UTF_8))) {
			return GSON.fromJson(reader, JsonObject.class);
		} catch (IOException e) {
			getLogger().error("[MigrateVersion101] Failed to read file for migrate", e);
		}
		return null;
	}

	private void migrateFields(JsonObject object) {
		JsonObject combination = getPatCombination(object);
		if (combination != null) {
			migrateKey(combination, "attributeKey");
			migrateKey(combination, "key");
		}

		JsonElement versionElement = object.get("version");
		if (versionElement != null && Version.of(versionElement.getAsString()).is(Version.of(MIGRATE_VERSION))) {
			object.addProperty("version", Version.CLIENT_CONFIG_VERSION.toString());
		}
	}

	private static JsonObject getPatCombination(JsonObject object) {
		if (!(object.get("main") instanceof JsonObject main)) {
			return null;
		}
		return main.get("patCombination") instanceof JsonObject combination ? combination : null;
	}

	private static boolean isLegacyKey(JsonElement element) {
		return element instanceof JsonObject;
	}

	private static void migrateKey(JsonObject combination, String field) {
		if (!(combination.get(field) instanceof JsonObject legacyKey)) {
			return;
		}
		int id = legacyKey.get("id") instanceof JsonPrimitive primitive ? primitive.getAsInt() : -1;
		String type = legacyKey.get("type") instanceof JsonPrimitive primitive ? primitive.getAsString() : "keysym";
		combination.addProperty(field, getKeyName(id, type));
	}

	private static String getKeyName(int id, String type) {
		return switch (type) {
			case "keysym" -> {
				String name = LEGACY_KEYSYM_NAMES.get(id);
				yield name == null ? UNKNOWN_KEY_NAME : "key.keyboard." + name;
			}
			case "mouse" -> switch (id) {
				case 0 -> "key.mouse.left";
				case 1 -> "key.mouse.right";
				case 2 -> "key.mouse.middle";
				default -> id > 2 ? "key.mouse." + (id + 1) : UNKNOWN_KEY_NAME;
			};
			default -> UNKNOWN_KEY_NAME;
		};
	}
}
