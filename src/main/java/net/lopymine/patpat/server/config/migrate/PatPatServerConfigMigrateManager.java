package net.lopymine.patpat.server.config.migrate;

import net.lopymine.patpat.*;
import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.common.migrate.*;
import net.lopymine.patpat.server.config.PatPatServerConfig;

public class PatPatServerConfigMigrateManager extends AbstractPatPatConfigMigrateManager {

	private static final PatPatServerConfigMigrateManager INSTANCE = new PatPatServerConfigMigrateManager();

	public PatPatServerConfigMigrateManager(PatPatServerConfig config, MigrateHandler... handlers) {
		super(PatPat.LOGGER, Version.SERVER_CONFIG_VERSION, config::getVersion);
		this.addHandlers(handlers);
	}

	private PatPatServerConfigMigrateManager() {
		super(PatPat.LOGGER, Version.SERVER_CONFIG_VERSION, () -> PatPatServerConfig.getInstance().getVersion());
		this.addHandlers(new PatPatServerConfigMigrateVersion0());
	}

	public static PatPatServerConfigMigrateManager getInstance() {
		return INSTANCE;
	}
}
