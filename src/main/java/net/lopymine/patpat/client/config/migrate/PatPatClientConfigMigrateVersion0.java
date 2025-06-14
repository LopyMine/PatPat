package net.lopymine.patpat.client.config.migrate;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.Setter;

import com.mojang.serialization.Codec;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.common.migrate.AbstractConfigMigrateHandler;
import net.lopymine.patpat.utils.*;

import java.io.*;
import java.util.*;

@Setter
public class PatPatClientConfigMigrateVersion0 extends AbstractConfigMigrateHandler {

	private static final String MIGRATE_FILE_NAME = "patpat-client.json5";
	private static final String MIGRATE_VERSION = "0";

	private PatPatClientConfig config = null;
	private PatPatClientPlayerListConfig playerListConfig = null;

	public PatPatClientConfigMigrateVersion0() {
		super(MIGRATE_FILE_NAME, MIGRATE_VERSION, PatPatClient.LOGGER);
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
		try {
			File migrateFile = this.getMigrateFile().toFile();
			JsonObject object = GSON.fromJson(new JsonReader(new FileReader(migrateFile)), JsonObject.class);
			PatPatClientConfig config = this.config == null ? PatPatClientConfig.getInstance() : this.config;
			PatPatClientPlayerListConfig playerListConfig = this.playerListConfig == null ? PatPatClientPlayerListConfig.getInstance() : this.playerListConfig;
			this.migrateFields(object, config, playerListConfig);
			return true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void migrateFields(JsonObject object, PatPatClientConfig config, PatPatClientPlayerListConfig playerListConfig) {
		HashMap<UUID, String> list = CodecUtils.decode("list", Codec.unboundedMap(VersionedThings.UUID_CODEC, Codec.STRING).xmap(HashMap::new, HashMap::new), object);
		Boolean bypassServerResourcePackPriorityEnabled = CodecUtils.decode("bypassServerResourcePackPriorityEnabled", Codec.BOOL, object);
		Boolean hidingNicknameEnabled = CodecUtils.decode("hidingNicknameEnabled", Codec.BOOL, object);
		Boolean swingHandEnabled = CodecUtils.decode("swingHandEnabled", Codec.BOOL, object);
		Boolean soundsEnabled = CodecUtils.decode("soundsEnabled", Codec.BOOL, object);
		Boolean patMeEnabled = CodecUtils.decode("patMeEnabled", Codec.BOOL, object);
		Boolean cameraShackingEnabled = CodecUtils.decode("cameraShackingEnabled", Codec.BOOL, object);
		Boolean modEnabled = CodecUtils.decode("modEnabled", Codec.BOOL, object);
		Float soundsVolume = CodecUtils.decode("soundsVolume", Codec.FLOAT, object);
		ListMode listMode = CodecUtils.decode("listMode", ListMode.CODEC, object);
		Float offsetX = CodecUtils.decode("offsetX", Codec.FLOAT, object);
		Float offsetY = CodecUtils.decode("offsetY", Codec.FLOAT, object);
		Float offsetZ = CodecUtils.decode("offsetZ", Codec.FLOAT, object);
		Boolean debugLogEnabled = CodecUtils.decode("debugLogEnabled", Codec.BOOL, object);
		Boolean skipOldAnimationsEnabled = CodecUtils.decode("skipOldAnimationsEnabled", Codec.BOOL, object);
		Float patWeight = CodecUtils.decode("patWeight", Codec.FLOAT, object);

		// SOUNDS CONFIG
		PatPatClientSoundsConfig soundsConfig = config.getSoundsConfig();
		if (soundsEnabled != null) {
			soundsConfig.setSoundsEnabled(soundsEnabled);
		}
		if (soundsVolume != null) {
			soundsConfig.setSoundsVolume(soundsVolume);
		}

		PatPatClientMainConfig mainConfig = config.getMainConfig();
		if (debugLogEnabled != null) {
			mainConfig.setDebugLogEnabled(debugLogEnabled);
		}
		if (modEnabled != null) {
			mainConfig.setModEnabled(modEnabled);
		}

		// SERVER CONFIG
		PatPatClientMultiplayerConfig serverConfig = config.getMultiPlayerConfig();
		if (bypassServerResourcePackPriorityEnabled != null) {
			serverConfig.setBypassServerResourcePackEnabled(bypassServerResourcePackPriorityEnabled);
		}
		if (patMeEnabled != null) {
			serverConfig.setPatMeEnabled(patMeEnabled);
		}
		if (listMode != null) {
			serverConfig.setListMode(listMode);
		}
		if (list != null) {
			Map<UUID, String> map = playerListConfig.getMap();
			map.clear();
			map.putAll(list);
		}

		// VISUAL CONFIG
		PatPatClientVisualConfig visualConfig = config.getVisualConfig();
		if (patWeight != null) {
			visualConfig.setPatWeight(patWeight);
		}
		if (offsetX != null && offsetY != null && offsetZ != null) {
			visualConfig.setAnimationOffsets(new Vec3f(offsetX, offsetY, offsetZ));
		}
		if (cameraShackingEnabled != null) {
			visualConfig.setCameraShackingEnabled(cameraShackingEnabled);
		}
		if (swingHandEnabled != null) {
			visualConfig.setSwingHandEnabled(swingHandEnabled);
		}
		if (hidingNicknameEnabled != null) {
			visualConfig.setHidingNicknameEnabled(hidingNicknameEnabled);
		}

		// RESOURCE PACKS CONFIG
		PatPatClientResourcePacksConfig resourcePacksConfig = config.getResourcePacksConfig();
		if (skipOldAnimationsEnabled != null) {
			resourcePacksConfig.setSkipOldAnimationsEnabled(skipOldAnimationsEnabled);
		}

		if (this.isShouldSave()) {
			config.save();
			playerListConfig.save();
		}
	}
}
