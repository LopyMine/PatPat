package net.lopymine.patpat.client.config.migrate;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.common.migrate.*;

public class PatPatClientConfigMigrateManager extends AbstractPatPatConfigMigrateManager {

	public PatPatClientConfigMigrateManager(PatPatClientConfig config, MigrateHandler... handlers) {
		super(PatPatClient.LOGGER, Version.CLIENT_CONFIG_VERSION, config::getVersion);
		this.addHandlers(handlers);
	}

	protected PatPatClientConfigMigrateManager() {
		super(PatPatClient.LOGGER, Version.CLIENT_CONFIG_VERSION, () -> PatPatClientConfig.getInstance().getVersion());
		this.addHandlers(new PatPatClientConfigMigrateVersion0());
	}

	private static final PatPatClientConfigMigrateManager INSTANCE = new PatPatClientConfigMigrateManager();

	public static PatPatClientConfigMigrateManager getInstance() {
		return INSTANCE;
	}
}
