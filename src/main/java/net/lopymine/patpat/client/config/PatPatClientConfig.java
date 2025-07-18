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

	public static final PatPatClientConfig DEFAULT = new PatPatClientConfig(
			Version.CLIENT_CONFIG_VERSION,
			PatPatClientMainConfig.DEFAULT,
			PatPatClientResourcePacksConfig.DEFAULT,
			PatPatClientSoundsConfig.DEFAULT,
			PatPatClientVisualConfig.DEFAULT,
			PatPatClientMultiplayerConfig.DEFAULT,
			PatPatClientProximityPacketsConfig.DEFAULT
	);

	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			option("version", DEFAULT.version, Version.CODEC, PatPatClientConfig::getVersion),
			option("main", (Supplier<PatPatClientMainConfig>) () -> DEFAULT.mainConfig.copy(), PatPatClientMainConfig.CODEC, PatPatClientConfig::getMainConfig),
			option("resourcePacks", (Supplier<PatPatClientResourcePacksConfig>) () -> DEFAULT.resourcePacksConfig.copy(), PatPatClientResourcePacksConfig.CODEC, PatPatClientConfig::getResourcePacksConfig),
			option("sounds", (Supplier<PatPatClientSoundsConfig>) () -> DEFAULT.soundsConfig.copy(), PatPatClientSoundsConfig.CODEC, PatPatClientConfig::getSoundsConfig),
			option("visual", (Supplier<PatPatClientVisualConfig>) () -> DEFAULT.visualConfig.copy(), PatPatClientVisualConfig.CODEC, PatPatClientConfig::getVisualConfig),
			option("multiplayer", (Supplier<PatPatClientMultiplayerConfig>) () -> DEFAULT.multiPlayerConfig.copy(), PatPatClientMultiplayerConfig.CODEC, PatPatClientConfig::getMultiPlayerConfig),
			option("proximityPackets", (Supplier<PatPatClientProximityPacketsConfig>) () -> DEFAULT.proximityPacketsConfig.copy(), PatPatClientProximityPacketsConfig.CODEC, PatPatClientConfig::getProximityPacketsConfig)
	).apply(inst, PatPatClientConfig::new));

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("Config");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-client.json5").toFile();
	private static PatPatClientConfig instance;

	private Version version;
	private PatPatClientMainConfig mainConfig;
	private PatPatClientResourcePacksConfig resourcePacksConfig;
	private PatPatClientSoundsConfig soundsConfig;
	private PatPatClientVisualConfig visualConfig;
	private PatPatClientMultiplayerConfig multiPlayerConfig;
	private PatPatClientProximityPacketsConfig proximityPacketsConfig;

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
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}
}
