package net.lopymine.patpat.client.config;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.CodecUtils;
import net.lopymine.patpat.utils.ConfigUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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
			option("proximityPackets", PatPatClientProximityPacketsConfig.getNewInstance(), PatPatClientProximityPacketsConfig.CODEC, PatPatClientConfig::getProximityPacketsConfig),
			option("fun", PatPatClientFunConfig.getNewInstance(), PatPatClientFunConfig.CODEC, PatPatClientConfig::getFunConfig)
	).apply(inst, PatPatClientConfig::new));

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("Config");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-client.json5").toFile();
	private static PatPatClientConfig instance;

	@Setter
	private Version version;
	private PatPatClientMainConfig mainConfig;
	private PatPatClientResourcePacksConfig resourcePacksConfig;
	private PatPatClientSoundsConfig soundsConfig;
	private PatPatClientVisualConfig visualConfig;
	private PatPatClientMultiplayerConfig multiPlayerConfig;
	private PatPatClientProximityPacketsConfig proximityPacketsConfig;
	private PatPatClientFunConfig funConfig;

	public static PatPatClientConfig getInstance() {
		return instance == null ? reload() : instance;
	}

	public static PatPatClientConfig reload() {
		instance = PatPatClientConfig.read();
		return instance;
	}

	public static Supplier<PatPatClientConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PatPatClientConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}
}
