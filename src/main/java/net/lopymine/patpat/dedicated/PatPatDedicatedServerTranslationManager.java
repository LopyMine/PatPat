package net.lopymine.patpat.dedicated;

import java.io.*;
import java.util.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.*;
import net.minecraft.locale.Language;

public class PatPatDedicatedServerTranslationManager {

	private static final Map<String, String> EN_US = new HashMap<>();

	private PatPatDedicatedServerTranslationManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void reload() {
		EN_US.clear();

		try (InputStream stream = EarlyCommonMultiLoader.getInstance().loadModFile(PatPat.MOD_ID, "assets/%s/lang/en_us.json".formatted(PatPat.MOD_ID))) {
			if (stream == null) {
				throw new IllegalArgumentException("Failed to load dedicated server language file");
			}
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
