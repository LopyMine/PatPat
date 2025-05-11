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

	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("version", Version.CLIENT_CONFIG_VERSION, Version.CODEC, PatPatClientConfig::getVersion),
			option("main", PatPatClientMainConfig.getNewInstance(), PatPatClientMainConfig.CODEC, PatPatClientConfig::getMainConfig),
			option("resourcePacks", PatPatClientResourcePacksConfig.getNewInstance(), PatPatClientResourcePacksConfig.CODEC, PatPatClientConfig::getResourcePacksConfig),
			option("sounds", PatPatClientSoundsConfig.getNewInstance(), PatPatClientSoundsConfig.CODEC, PatPatClientConfig::getSoundsConfig),
			option("visual", PatPatClientVisualConfig.getNewInstance(), PatPatClientVisualConfig.CODEC, PatPatClientConfig::getVisualConfig),
			option("server", PatPatClientServerConfig.getNewInstance(), PatPatClientServerConfig.CODEC, PatPatClientConfig::getServerConfig)
	).apply(instance, PatPatClientConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-client.json5").toFile();
	private static PatPatClientConfig INSTANCE;

	private Version version;
	private PatPatClientMainConfig mainConfig;
	private PatPatClientResourcePacksConfig resourcePacksConfig;
	private PatPatClientSoundsConfig soundsConfig;
	private PatPatClientVisualConfig visualConfig;
	private PatPatClientServerConfig serverConfig;

	private PatPatClientConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static PatPatClientConfig reload() {
		return INSTANCE = PatPatClientConfig.read();
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
