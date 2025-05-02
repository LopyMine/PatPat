package net.lopymine.patpat.server.config.sub;

import lombok.Getter;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.config.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@Getter
public class PatPatServerPlayerListConfig {

	private static final String FILENAME = "player-list.txt";
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
	private static PatPatServerPlayerListConfig INSTANCE;

	private final Set<UUID> uuids = new HashSet<>();

	public static PatPatServerPlayerListConfig getInstance() {
		return INSTANCE;
	}

	private static @NotNull PatPatServerPlayerListConfig create() {
		try {
			if (CONFIG_FILE.createNewFile()) {
				PatPat.LOGGER.error(FILENAME + " already exists");
			}
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to create " + FILENAME, e);
		}
		return new PatPatServerPlayerListConfig();
	}

	public static void reload() {
		if (!CONFIG_FILE.exists()) {
			INSTANCE = PatPatServerPlayerListConfig.create();
		}

		PatPatServerPlayerListConfig config = new PatPatServerPlayerListConfig();
		int lineNumber = 0;
		String line = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				config.getUuids().add(UUID.fromString(line));
				line = reader.readLine();
			}
			INSTANCE = config;
			return;
		} catch (IllegalArgumentException e) {
			PatPat.LOGGER.error("Error line %d: '%s' is not uuid, file %s".formatted(lineNumber, line == null ? "null" : line, FILENAME));
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to read " + FILENAME, e);
		}

		INSTANCE = PatPatServerPlayerListConfig.create();
	}

	public boolean contains(UUID uuid){
		return this.uuids.contains(uuid);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		save(CONFIG_FILE);
	}

	public void save(File folder) {
		try (FileWriter writer = new FileWriter(folder, StandardCharsets.UTF_8)) {
			writer.write(uuids.stream().map(UUID::toString).collect(Collectors.joining("\n")));
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to save " + FILENAME, e);
		}
	}
}
