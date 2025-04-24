package net.lopymine.patpat.server.config.migrate;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.common.migrate.MigrateHandler;
import net.lopymine.patpat.server.config.PatPatServerConfig;

import java.util.*;

public class PatPatServerConfigMigrateManager {

	private static final Set<MigrateHandler> HANDLERS = new HashSet<>();

	private PatPatServerConfigMigrateManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		HANDLERS.clear();
		addHandlers(new PatPatServerConfigMigrateVersion0());
	}

	public static void migrate() {
		for (MigrateHandler handler : HANDLERS) {
			if (!handler.needMigrate()) {
				continue;
			}
			String migrateVersion = handler.getVersion();
			if (!handler.migrate()) {
				PatPat.LOGGER.warn("Error migrate config from version: " + migrateVersion);
				PatPat.LOGGER.warn("Report the issue at github page: {}, attaching your config and specifying the mod and server versions.", PatPat.ISSUE_LINK);
				return;
			}
			PatPat.LOGGER.info("Config successful migrated from version: " + migrateVersion);
		}

		Version version = PatPatServerConfig.getInstance().getVersion();
		if (version.is(Version.SERVER_CONFIG_VERSION)) {
			return;
		}
		if (version.isMoreThan(Version.SERVER_CONFIG_VERSION)) {
			PatPat.LOGGER.warn("Your config version is higher than the mod's (%s > %s). This may cause errors!".formatted(version, Version.SERVER_CONFIG_VERSION));
			PatPat.LOGGER.warn("Update the mod to avoid issues.");
			return;
		}
		PatPat.LOGGER.warn("Your config version is lower than the mod's (%s < %s). This may cause errors!".formatted(version, Version.SERVER_CONFIG_VERSION));
		PatPat.LOGGER.warn("Back up your config and report the issue at github page: {}, attaching your config and specifying the mod and server versions.", PatPat.ISSUE_LINK);
	}

	private static void addHandlers(MigrateHandler... handlers) {
		HANDLERS.addAll(List.of(handlers));
	}


}
