package net.lopymine.patpat.config.server.migrate;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.Version;
import net.lopymine.patpat.config.server.PatPatServerConfig;

import java.util.*;

public class MigrateServerConfigManager {

	private static final Set<MigrateHandler> HANDLERS = new HashSet<>();

	private MigrateServerConfigManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void onInitialize() {
		HANDLERS.clear();
		addHandlers(new MigrateVersion0());
	}

	public static void migrate() {
		for (MigrateHandler handler : HANDLERS) {
			handler.migrate();
		}
		Version version = PatPatServerConfig.getInstance().getVersion();
		if (!version.is(Version.SERVER_CONFIG_VERSION)) {
			if (version.isMoreThan(Version.SERVER_CONFIG_VERSION)) {
				PatPat.LOGGER.warn("Your config version is higher than the mod's (%s > %s). This may cause errors!".formatted(version, Version.SERVER_CONFIG_VERSION));
				PatPat.LOGGER.warn("Update the mod to avoid issues.");
				return;
			}
			PatPat.LOGGER.warn("Your config version is lower than the mod's (%s < %s). This may cause errors!".formatted(version, Version.SERVER_CONFIG_VERSION));
			PatPat.LOGGER.warn("Back up your config and report the issue at github page, attaching your config and specifying the mod and server versions.");
		}
	}

	private static void addHandlers(MigrateHandler... handlers) {
		HANDLERS.addAll(List.of(handlers));
	}


}
