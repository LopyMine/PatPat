package net.lopymine.patpat.client.config.sub;

import lombok.Getter;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.PlayerListConfigUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public class PatPatClientPlayerListConfig {

	private static final String FILENAME = "player-list-client.txt";
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
	private static PatPatClientPlayerListConfig INSTANCE;

	private final Map<UUID, String> map = new HashMap<>();

	public static PatPatClientPlayerListConfig getInstance() {
		if (INSTANCE == null) {
			return reload();
		}
		return INSTANCE;
	}

	public static PatPatClientPlayerListConfig reload() {
		PatPatClientPlayerListConfig config = new PatPatClientPlayerListConfig();
		PlayerListConfigUtils.read(CONFIG_FILE, PatPatClient.LOGGER, config.getMap());
		return INSTANCE = config;
	}

	public boolean contains(UUID uuid){
		return this.map.containsKey(uuid);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		this.save(CONFIG_FILE);
	}

	public void save(File configFile) {
		PlayerListConfigUtils.save(configFile, PatPatClient.LOGGER, this.map);
	}
}
