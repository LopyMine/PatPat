package net.lopymine.patpat.client.config;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.CodecUtils;
import net.lopymine.patpat.utils.ConfigUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Setter
@Getter
@AllArgsConstructor
public class PatPatDebugConfig {

	public static final Codec<PatPatDebugConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			option("selfPat", false, Codec.BOOL, PatPatDebugConfig::isSelfPat)
	).apply(inst, PatPatDebugConfig::new));

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("Debug-Config");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(PatPat.MOD_ID + "-debug.json5").toFile();
	private static PatPatDebugConfig instance;

	private boolean selfPat;

	public static PatPatDebugConfig getInstance() {
		return instance == null ? reload() : instance;
	}

	public static PatPatDebugConfig reload() {
		instance = PatPatDebugConfig.read();
		return instance;
	}

	public static Supplier<PatPatDebugConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PatPatDebugConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}

}
