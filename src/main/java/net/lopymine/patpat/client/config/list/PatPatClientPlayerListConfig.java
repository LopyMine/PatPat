package net.lopymine.patpat.client.config.list;

import java.io.File;
import java.util.HashMap;
import lombok.Getter;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.common.config.list.AbstractPatPatPlayerListConfig;
import net.lopymine.patpat.logger.PatLogger;

@Getter
public class PatPatClientPlayerListConfig extends AbstractPatPatPlayerListConfig {

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend(PatPatClientPlayerListConfig.class.getSimpleName());
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("player-list-client.txt").toFile();
	private static final PatPatClientPlayerListConfig INSTANCE = new PatPatClientPlayerListConfig();

	public PatPatClientPlayerListConfig() {
		super(new HashMap<>(), LOGGER, CONFIG_FILE);
	}

	public static PatPatClientPlayerListConfig getInstance() {
		return getInitialized(INSTANCE);
	}

}
