package net.lopymine.patpat.manager;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.PatPat;

import java.io.File;
import java.nio.file.Path;

public class PatPatConfigManager {
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("patpat/");

	public static void onInitialize() {
		File file = PatPatConfigManager.CONFIG_PATH.toFile();
		if (file.exists()) {
			return;
		}
		if (file.mkdirs()) {
			PatPat.LOGGER.info("Successfully created PatPat config folder");
		}
	}
}
