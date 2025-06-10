package net.lopymine.patpat.server.config;

import lombok.*;
import lombok.experimental.ExtensionMethod;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.common.config.*;
import net.lopymine.patpat.extension.GsonExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.utils.*;

import java.io.*;
import java.util.concurrent.CompletableFuture;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@AllArgsConstructor
@ExtensionMethod(GsonExtension.class)
public class PatPatServerConfig {

	public static final Codec<PatPatServerConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			option("debug", false, Codec.BOOL, PatPatServerConfig::isDebugModeEnabled),
			option("version", Version.SERVER_CONFIG_VERSION, Version.CODEC, PatPatServerConfig::getVersion),
			option("listMode", ListMode.DISABLED, ListMode.CODEC, PatPatServerConfig::getListMode),
			option("rateLimit", new PatPatServerRateLimitConfig(), PatPatServerRateLimitConfig.CODEC, PatPatServerConfig::getRateLimitConfig)
	).apply(inst, PatPatServerConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + ".json5").toFile();
	private static PatPatServerConfig INSTANCE;

	private boolean debugModeEnabled;
	@Getter
	private Version version;
	@Setter
	private ListMode listMode;
	private PatPatServerRateLimitConfig rateLimitConfig;

	private PatPatServerConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatServerConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static PatPatServerConfig reload() {
		return INSTANCE = PatPatServerConfig.read();
	}

	public static PatPatServerConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PatPatServerConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, PatPat.LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, PatPat.LOGGER);
	}
}
