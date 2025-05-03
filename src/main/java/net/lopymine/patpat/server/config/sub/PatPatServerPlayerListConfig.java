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
				PatPat.LOGGER.error("Invoked create player-list.txt config, but config already exists!");
			}
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to create player-list.txt config!", e);
		}
		return new PatPatServerPlayerListConfig();
	}

	public static PatPatServerPlayerListConfig reload() {
		return INSTANCE = read();
	}

	public static PatPatServerPlayerListConfig read() {
		if (!CONFIG_FILE.exists()) {
			return PatPatServerPlayerListConfig.create();
		}

		PatPatServerPlayerListConfig config = new PatPatServerPlayerListConfig();

		try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			int lineIndex = 0;

			while ((line = reader.readLine()) != null) {
				lineIndex++;
				try {
					config.getUuids().add(UUID.fromString(line.trim()));
				} catch (Exception e) {
					PatPat.LOGGER.error("Failed to parse line {}: '{}' is not UUID while parsing {}", lineIndex, line, FILENAME);
				}
			}
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to read " + FILENAME, e);
			return PatPatServerPlayerListConfig.create();
		}

		return config;
	}

	public boolean contains(UUID uuid){
		return this.uuids.contains(uuid);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		this.save(CONFIG_FILE);
	}

	public void save(File folder) {
		try (FileWriter writer = new FileWriter(folder, StandardCharsets.UTF_8)) {
			writer.write(this.uuids.stream().map(UUID::toString).collect(Collectors.joining("\n")));
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to save " + FILENAME, e);
		}
	}
}
