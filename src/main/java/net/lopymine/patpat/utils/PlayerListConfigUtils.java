package net.lopymine.patpat.utils;

import net.lopymine.patpat.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerListConfigUtils {

	public static void create(File config, PatLogger logger) {
		try {
			if (config.createNewFile()) {
				logger.error("Invoked create player-list.txt config, but config already exists!");
			}
		} catch (Exception e) {
			logger.error("Failed to create player-list.txt config!", e);
		}
	}

	public static void read(File config, PatLogger logger, Map<UUID, String> map) {
		if (!config.exists()) {
			PlayerListConfigUtils.create(config, logger);
			return;
		}

		int lineNumber = 0;
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
			line = reader.readLine();
			while (line != null) {
				lineNumber++;
				try {
					String[] uuidNicknamePair = line.split(" ");
					map.put(UUID.fromString(uuidNicknamePair[0]), uuidNicknamePair[1]);
				} catch (Exception e) {
					logger.error("Failed to parse line {} while parsing PlayerListConfig:", lineNumber, line, config.getName());
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			logger.error("Failed to reload PlayerListConfig:", e);
		}
	}

	public static void save(File config, PatLogger logger, Map<UUID, String> map) {
		try (FileWriter writer = new FileWriter(config, StandardCharsets.UTF_8)) {
			String collect = map
					.entrySet()
					.stream()
					.map(entry -> "%s %s".formatted(entry.getKey(), entry.getValue()))
					.collect(Collectors.joining("\n"));
			logger.debug("Saving list with players:");
			logger.debug(collect);
			writer.write(collect
			);
		} catch (Exception e) {
			logger.error("Failed to save PlayerListConfig with name " + config.getName(), e);
		}
	}
}
