package net.lopymine.patpat.manager;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.server.migrate.MigrateServerConfigManager;
import net.lopymine.patpat.config.server.*;

import java.io.File;
import java.nio.file.Path;

public class PatPatConfigManager {

	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("patpat/");

	private PatPatConfigManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		File file = PatPatConfigManager.CONFIG_PATH.toFile();
		if (file.exists()) {
			return;
		}
		if (file.mkdirs()) {
			PatPat.LOGGER.info("Successfully created PatPat config folder");
		}
	}

	public static void reload(){
		PlayerListConfig.reload();
		PatPatServerConfig.reload();
		MigrateServerConfigManager.onInitialize();
		MigrateServerConfigManager.migrate();
	}
}
