package net.lopymine.patpat.common.config;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.*;
import net.lopymine.patpat.client.config.list.PatPatClientProxLibServersWhitelistConfig;
import net.lopymine.patpat.client.config.migrate.PatPatClientConfigMigrateManager;
import net.lopymine.patpat.entrypoint.*;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.migrate.PatPatServerConfigMigrateManager;
import net.lopymine.patpat.server.config.list.PatPatServerPlayerListConfig;

import java.io.File;
import java.nio.file.Path;

public class PatPatConfigManager {

	public static final Path CONFIG_PATH = EarlyCommonMultiLoader.getInstance().getConfigDir().resolve("%s/".formatted(PatPat.MOD_ID));

	private PatPatConfigManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		ServerMultiLoader.getInstance().registerOnServerStop(() -> PatPatServerConfig.getInstance().saveAsync());

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
		if(PatPatDebugConfig.DEBUG_ENABLED){
			PatPatDebugConfig.reload();
		}
	}
}
