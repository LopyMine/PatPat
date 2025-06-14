package net.lopymine.patpat.client.config;

import lombok.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.common.config.*;
import net.lopymine.patpat.utils.*;

import java.io.*;
import java.util.concurrent.CompletableFuture;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@AllArgsConstructor
public class PatPatClientConfig {

	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			option("version", Version.CLIENT_CONFIG_VERSION, Version.CODEC, PatPatClientConfig::getVersion),
			option("main", PatPatClientMainConfig.getNewInstance(), PatPatClientMainConfig.CODEC, PatPatClientConfig::getMainConfig),
			option("resourcePacks", PatPatClientResourcePacksConfig.getNewInstance(), PatPatClientResourcePacksConfig.CODEC, PatPatClientConfig::getResourcePacksConfig),
			option("sounds", PatPatClientSoundsConfig.getNewInstance(), PatPatClientSoundsConfig.CODEC, PatPatClientConfig::getSoundsConfig),
			option("visual", PatPatClientVisualConfig.getNewInstance(), PatPatClientVisualConfig.CODEC, PatPatClientConfig::getVisualConfig),
			option("multiplayer", PatPatClientMultiplayerConfig.getNewInstance(), PatPatClientMultiplayerConfig.CODEC, PatPatClientConfig::getMultiPlayerConfig),
			option("proximityPackets", PatPatClientProximityPacketsConfig.getNewInstance(), PatPatClientProximityPacketsConfig.CODEC, PatPatClientConfig::getProximityPacketsConfig)
	).apply(inst, PatPatClientConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-client.json5").toFile();
	private static PatPatClientConfig instance;

	private Version version;
	private PatPatClientMainConfig mainConfig;
	private PatPatClientResourcePacksConfig resourcePacksConfig;
	private PatPatClientSoundsConfig soundsConfig;
	private PatPatClientVisualConfig visualConfig;
	private PatPatClientMultiplayerConfig multiPlayerConfig;
	private PatPatClientProximityPacketsConfig proximityPacketsConfig;

	private PatPatClientConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientConfig getInstance() {
		return instance == null ? reload() : instance;
	}

	public static PatPatClientConfig reload() {
		instance = PatPatClientConfig.read();
		return instance;
	}

	public static PatPatClientConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PatPatClientConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, PatPatClient.LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, PatPatClient.LOGGER);
	}
}
