package net.lopymine.patpat.server.config.migrate;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.Setter;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.common.migrate.AbstractConfigMigrateHandler;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.PatPatServerPlayerListConfig;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Setter
public class PatPatServerConfigMigrateVersion0 extends AbstractConfigMigrateHandler {

	private static final String MIGRATE_FILE_NAME = "patpat.json5";
	private static final String MIGRATE_VERSION = "0";

	private PatPatServerConfig config;
	private PatPatServerPlayerListConfig listConfig;

	public PatPatServerConfigMigrateVersion0() {
		super(MIGRATE_FILE_NAME, MIGRATE_VERSION, PatPat.LOGGER);
	}

	@Override
	public boolean needToMigrateFile() {
		try {
			File migrateFile = this.getMigrateFile().toFile();
			JsonObject object = GSON.fromJson(new JsonReader(new FileReader(migrateFile)), JsonObject.class);
			return !object.has("version");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean migrateFile() {
		File migrateFile = this.getMigrateFile().toFile();
		Configs configs = this.transform(migrateFile);
		if (configs == null) {
			return false;
		}
		if (this.isShouldSave()) {
			configs.saveAndReload();
		}
		return true;
	}

	public record Configs(PatPatServerConfig config, PatPatServerPlayerListConfig playerListConfig) {

		public void saveAndReload() {
			this.config.save();
			PatPatServerConfig.reload();

			this.playerListConfig.saveAsync();
			PatPatServerPlayerListConfig.reload();
		}

	}

	public Configs transform(File file) {
		Configs configs = null;
		AtomicBoolean success = new AtomicBoolean(true);
		try (FileReader reader = new FileReader(file)) {
			JsonObject rootObj = /*? <=1.17.1 {*//*new JsonParser().parse*//*?} else {*/JsonParser.parseReader/*?}*/(reader).getAsJsonObject();

			String listModeStr = rootObj.get("listMode").getAsString();
			PatPatServerConfig config = this.config == null ? PatPatServerConfig.getNewInstance() : this.config;
			config.setListMode(ListMode.getByIdOrDefault(listModeStr, ListMode.DISABLED));

			PatPatServerPlayerListConfig playerListConfig = this.listConfig == null ? new PatPatServerPlayerListConfig() : this.listConfig;
			Map<UUID, String> map = playerListConfig.getMap();
			Map<UUID, String> oldConfigMap = new HashMap<>();
			JsonObject jsonObject = rootObj.getAsJsonObject("list");

			jsonObject/*? if >=1.19.3 {*/.asMap()/*?}*/.entrySet().forEach(entry -> {
				try {
					String uuid = entry.getKey();
					String nickname = entry.getValue().getAsString();

					String uuidString = new StringBuilder(uuid)
							.insert(8, "-")
							.insert(12, "-")
							.insert(16, "-")
							.insert(20, "-")
							.toString();
					oldConfigMap.put(UUID.fromString(uuidString), nickname);
				} catch (IllegalArgumentException e) {
					PatPat.LOGGER.error("Line '%s' cannot parse uuid".formatted(entry), e);
					success.set(false);
				}
			});
			map.clear();
			map.putAll(oldConfigMap);
			if (success.get()) {
				configs = new Configs(config, playerListConfig);
			}
		} catch (IOException e) {
			PatPat.LOGGER.error("Failed to read " + MIGRATE_FILE_NAME, e);
			return null;
		}
		return configs;
	}
}
