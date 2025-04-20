package net.lopymine.patpat.server.config.migrate;

import com.google.gson.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.common.migrate.MigrateHandler;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatPatServerConfigMigrateVersion0 implements MigrateHandler {

	private static final String FILENAME = "patpat.json5";
	private static final String BACKUP_FILENAME = FILENAME + ".bkp";
	private static final String VERSION = "0";

	public record Configs(PatPatServerConfig config, PatPatServerPlayerListConfig playerListConfig) {

		public void saveAndReload() {
			config.save();
			PatPatServerConfig.reload();
			playerListConfig.save();
			PatPatServerPlayerListConfig.reload();
		}

	}

	private boolean createBackup(File file) {
		try {
			Files.copy(file.toPath(), PatPatConfigManager.CONFIG_PATH.resolve(BACKUP_FILENAME), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to create old config backup", e);
			return false;
		}
		return true;
	}

	public Configs transform(File file) {
		Configs configs = null;
		AtomicBoolean success = new AtomicBoolean(true);
		try (FileReader reader = new FileReader(file)) {
			JsonObject rootObj = /*? <=1.17.1 {*//*new JsonParser().parse*//*?} else {*/JsonParser.parseReader/*?}*/(reader).getAsJsonObject();

			String listModeStr = rootObj.get("listMode").getAsString();
			PatPatServerConfig config = new PatPatServerConfig();
			config.setListMode(ListMode.getByIdOrDefault(listModeStr, ListMode.DISABLED));

			PatPatServerPlayerListConfig playerListConfig = new PatPatServerPlayerListConfig();
			Set<UUID> uuids = playerListConfig.getUuids();
			Set<UUID> uuidsFromOldConfig = new HashSet<>();
			JsonObject jsonObject = rootObj.getAsJsonObject("list");

			jsonObject.
					//? <=1.19.2 {
					/*entrySet().forEach(entry->{
						String s = entry.getKey();
					*//*?} else {*/
					asMap().keySet().forEach(s -> {
				//?}
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
					success.set(false);
				}
			});
			uuids.clear();
			uuids.addAll(uuidsFromOldConfig);
			if (success.get()) {
				configs = new Configs(config, playerListConfig);
			}
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to read " + FILENAME, e);
			return null;
		}
		return configs;
	}

	public boolean needMigrate() {
		File oldFile = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
		return oldFile.exists();
	}

	public boolean migrate() {
		File oldFile = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
		if (!oldFile.exists()) {
			return false;
		}
		PatPat.LOGGER.warn("Find outdated config (v0), migrate... (old config fill saved as '%s')".formatted(BACKUP_FILENAME));
		if (!createBackup(oldFile)) {
			return false;
		}
		Configs configs = transform(oldFile);
		if (configs == null) {
			return false;
		}
		configs.saveAndReload();
		return true;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}
}
