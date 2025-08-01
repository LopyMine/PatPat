package net.lopymine.patpat.dedicated;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import net.fabricmc.loader.api.*;
import net.lopymine.patpat.PatPat;
import net.minecraft.locale.Language;

public class PatPatDedicatedServerTranslationManager {

	private static final Map<String, String> EN_US = new HashMap<>();

	private PatPatDedicatedServerTranslationManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void reload() {
		EN_US.clear();

		ModContainer modContainer = FabricLoader.getInstance().getModContainer(PatPat.MOD_ID).orElse(null);
		if (modContainer == null) {
			PatPatDedicatedServer.LOGGER.error("Failed to get PatPat language files for server-side, because PatPat mod container doesn't exits!");
			return;
		}
		Optional<Path> optional = modContainer.findPath("assets/%s/lang/en_us.json".formatted(PatPat.MOD_ID));
		if (optional.isEmpty()) {
			PatPatDedicatedServer.LOGGER.error("Failed to find PatPat language files for server-side!");
			return;
		}

		try (InputStream stream = Files.newInputStream(optional.get())){
			Language.loadFromJson(stream, EN_US::put);
		} catch (Exception e) {
			PatPatDedicatedServer.LOGGER.error("Unexpected error when parsing PatPat language file: ", e);
		}

		PatPatDedicatedServer.LOGGER.info("Successfully loaded PatPat language files!");
	}

	public static String getOrDefault(String key) {
		return PatPatDedicatedServerTranslationManager.EN_US.getOrDefault(key, key);
	}

}
