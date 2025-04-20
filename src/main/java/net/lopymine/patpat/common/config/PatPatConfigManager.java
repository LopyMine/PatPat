package net.lopymine.patpat.common.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.migrate.PatPatServerConfigMigrateManager;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;

import java.io.File;
import java.nio.file.Path;

public class PatPatConfigManager {

	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("patpat/");

	private PatPatConfigManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		ServerLifecycleEvents.SERVER_STOPPING.register(s -> PatPatServerConfig.getInstance().saveAsync());
		File file = PatPatConfigManager.CONFIG_PATH.toFile();
		if (!file.exists() && file.mkdirs()) {
			PatPat.LOGGER.info("Successfully created PatPat config folder");
		}
	}

	public static void reload() {
		PatPatServerPlayerListConfig.reload();
		PatPatServerConfig config = PatPatServerConfig.reload();
		PatPatServerConfigMigrateManager.onInitialize();
		PatPatServerConfigMigrateManager.migrate();
		PatPat.LOGGER.setDebugMode(config.isDebugMode());
	}
}
