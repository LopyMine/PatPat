package net.lopymine.patpat.server.config.list;

import lombok.Getter;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.common.config.list.*;

import java.io.*;
import java.util.*;

@Getter
public class PatPatServerPlayerListConfig extends AbstractPatPatPlayerListConfig {

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend(PatPatServerPlayerListConfig.class.getSimpleName());
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("player-list.txt").toFile();
	private static final PatPatServerPlayerListConfig INSTANCE = new PatPatServerPlayerListConfig();

	public static PatPatServerPlayerListConfig getInstance() {
		return getInitialized(INSTANCE);
	}

	public PatPatServerPlayerListConfig() {
		super(new HashMap<>(), LOGGER, CONFIG_FILE);
	}

}