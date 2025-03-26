package net.lopymine.patpat.config.server.migrate;

import com.google.gson.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MigrateVersion0 implements MigrateHandler {

	private static final String FILENAME = "patpat.json5";

	public void migrate() {
		Path path = PatPatConfigManager.CONFIG_PATH;
		File oldFile = path.resolve(FILENAME).toFile();
		if (!oldFile.exists()) {
			return;
		}
		String backupFilename = FILENAME + ".bkp";
		PatPat.LOGGER.warn("Find outdated config (v0), migrate... (old config fill saved as '%s')".formatted(backupFilename));
		try {
			Files.copy(oldFile.toPath(), path.resolve(backupFilename), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to create old config backup", e);
			return;
		}
		boolean success = false;
		try (FileReader reader = new FileReader(oldFile)) {
			JsonObject rootObj = JsonParser.parseReader(reader).getAsJsonObject();

			String listModeStr = rootObj.get("listMode").getAsString();
			PatPatServerConfig config = PatPatServerConfig.getInstance();
			config.setListMode(ListMode.getByIdOrDefault(listModeStr, ListMode.DISABLED));
			config.save();

			PlayerListConfig playerListConfig = PlayerListConfig.getInstance();
			Set<UUID> uuids = playerListConfig.getUuids();
			Set<UUID> uuidsFromOldConfig = new HashSet<>();
			rootObj.getAsJsonObject("list").asMap().keySet().forEach(s -> {
				try {
					String uuidString = new StringBuilder(s)
							.insert(8, "-")
							.insert(12, "-")
							.insert(16, "-")
							.insert(20, "-")
							.toString();
					uuidsFromOldConfig.add(UUID.fromString(uuidString));
				} catch (IllegalArgumentException e) {
					PatPat.LOGGER.error("Line '%s' cannot parse uuid".formatted(s), e);
				}
			});
			uuids.clear();
			uuids.addAll(uuidsFromOldConfig);
			playerListConfig.save();
			success = true;
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to read " + FILENAME, e);
		}

		if (success) {
			PatPat.LOGGER.info("Config successful migrated");
			try {
				Files.delete(oldFile.toPath());
			} catch (IOException e) {
				PatPat.LOGGER.error("Failed to read " + FILENAME, e);
			}
		}
	}
}
