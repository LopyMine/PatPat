package net.lopymine.patpat.common.migrate;

import net.lopymine.patpat.*;
import net.lopymine.patpat.common.config.Version;

import java.util.*;
import java.util.function.Supplier;

public abstract class AbstractPatPatConfigMigrateManager {

	private final Set<MigrateHandler> handlers = new HashSet<>();
	private final PatLogger logger;
	private final Version currentGlobalConfigVersion;
	private final Supplier<Version> currentConfigVersionSupplier;

	protected AbstractPatPatConfigMigrateManager(PatLogger logger, Version currentGlobalConfigVersion, Supplier<Version> currentConfigVersionSupplier) {
		this.logger = logger;
		this.currentGlobalConfigVersion = currentGlobalConfigVersion;
		this.currentConfigVersionSupplier = currentConfigVersionSupplier;
	}

	public void migrate() {
		for (MigrateHandler handler : this.handlers) {
			if (!handler.needMigrate()) {
				continue;
			}
			String migrateVersion = handler.getMigrateVersion();
			if (!handler.migrate()) {
				this.logger.warn("Error migrate config from version: " + migrateVersion);
				this.logger.warn("Report the issue at github page: {}, attaching your config and specifying the mod and server versions.", PatPat.ISSUE_LINK);
				return;
			}
			this.logger.info("Config successful migrated from version: " + migrateVersion);
		}

		Version currentVersion = this.currentConfigVersionSupplier.get();
		Version globalVersion = this.currentGlobalConfigVersion;
		if (currentVersion.is(globalVersion)) {
			return;
		}
		if (currentVersion.isMoreThan(globalVersion)) {
			this.logger.warn("Your config version is higher than the mod's (%s > %s). This may cause errors!".formatted(currentVersion, globalVersion));
			this.logger.warn("Update the mod to avoid issues.");
			return;
		}
		this.logger.warn("Your config version is lower than the mod's (%s < %s). This may cause errors!".formatted(currentVersion, globalVersion));
		this.logger.warn("Back up your config and report the issue at github page: {}, attaching your config and specifying the mod and server versions.", PatPat.ISSUE_LINK);
	}

	protected void addHandlers(MigrateHandler... array) {
		handlers.addAll(List.of(array));
	}
}
