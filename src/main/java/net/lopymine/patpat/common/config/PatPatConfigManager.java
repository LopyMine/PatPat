package net.lopymine.patpat.common.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.list.PatPatClientProxLibServersWhitelistConfig;
import net.lopymine.patpat.client.config.migrate.PatPatClientConfigMigrateManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.migrate.PatPatServerConfigMigrateManager;
import net.lopymine.patpat.server.config.list.PatPatServerPlayerListConfig;

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

	public static void reloadServer() {
		PatPatServerConfigMigrateManager.getInstance().migrate();
		PatPatServerPlayerListConfig.getInstance().reload();
		PatPatServerConfig config = PatPatServerConfig.reload();
		PatPat.LOGGER.setDebugMode(config.isDebugModeEnabled());
	}

	public static void reloadClient() {
		PatPatClientConfigMigrateManager.getInstance().migrate();
		PatPatClientConfig config = PatPatClientConfig.reload();
		PatPatClientProxLibServersWhitelistConfig.getInstance().reload();
		PatPatClient.LOGGER.setDebugMode(config.getMainConfig().isDebugLogEnabled());
	}
}
