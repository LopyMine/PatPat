package net.lopymine.patpat.config.server;

import lombok.Getter;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class PlayerListConfig {

	@Getter
	private static PlayerListConfig instance;

	@Getter
	private final Set<UUID> uuids = new HashSet<>();

	private static final String FILENAME = "player-list.txt";
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();

	public boolean contains(UUID uuid){
		return uuids.contains(uuid);
	}


	private static @NotNull PlayerListConfig create() {
		try {
			if (CONFIG_FILE.createNewFile()) {
				PatPat.LOGGER.error(FILENAME + " already exists");
			}
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to create " + FILENAME, e);
		}
		return new PlayerListConfig();
	}

	public static void reload() {
		if (!CONFIG_FILE.exists()) {
			instance = PlayerListConfig.create();
		}

		PlayerListConfig config = new PlayerListConfig();
		int lineNumber = 0;
		String line = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				config.uuids.add(UUID.fromString(line));
				line = reader.readLine();
			}
			instance = config;
			return;
		} catch (IllegalArgumentException e) {
			PatPat.LOGGER.error("Error line %d: '%s' is not uuid, file %s".formatted(lineNumber, line == null ? "null" : line, FILENAME));
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to read " + FILENAME, e);
		}
		instance = PlayerListConfig.create();
	}

	public void save() {
		CompletableFuture.runAsync(() -> {
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				writer.write(uuids.stream().map(UUID::toString).collect(Collectors.joining("\n")));
			} catch (Exception e) {
				PatPat.LOGGER.error("Failed to save " + FILENAME, e);
			}
		});
	}
}
