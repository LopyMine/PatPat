package net.lopymine.patpat.server.config;

import lombok.Getter;

import net.lopymine.patpat.*;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.PlayerListConfigUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public class PatPatServerPlayerListConfig {

	private static final String FILENAME = "player-list.txt";
	private static final PatLogger LOGGER = PatPat.LOGGER.extend("PlayerListConfig");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
	private static PatPatServerPlayerListConfig INSTANCE;

	private final Map<UUID, String> map = new HashMap<>();

	public static PatPatServerPlayerListConfig getInstance() {
		if (INSTANCE == null) {
			return reload();
		}
		return INSTANCE;
	}

	public static PatPatServerPlayerListConfig reload() {
		PatPatServerPlayerListConfig config = new PatPatServerPlayerListConfig();
		PlayerListConfigUtils.read(CONFIG_FILE, LOGGER, config.getMap());
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

	public void save(File folder) {
		PlayerListConfigUtils.save(folder, LOGGER, this.map);
	}
}
