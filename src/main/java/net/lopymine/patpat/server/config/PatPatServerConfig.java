package net.lopymine.patpat.server.config;

import com.google.gson.*;
import lombok.*;
import lombok.experimental.ExtensionMethod;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.common.config.*;
import net.lopymine.patpat.extension.GsonExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.utils.CodecUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

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

	private static final String FILENAME = PatPat.MOD_ID + ".json";
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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

	public static PatPatServerConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public static PatPatServerConfig getInstance() {
		if (INSTANCE == null) {
			return reload();
		}
		return INSTANCE;
	}

	public static PatPatServerConfig reload() {
		INSTANCE = PatPatServerConfig.read();
		return INSTANCE;
	}

	private static @NotNull PatPatServerConfig create() {
		PatPatServerConfig config = PatPatServerConfig.getNewInstance();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(config));
			writer.write(json);
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static PatPatServerConfig read() {
		if (!CONFIG_FILE.exists()) {
			return PatPatServerConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, /*? <=1.17.1 {*//*new JsonParser().parse(reader)*//*?} else {*/JsonParser.parseReader(reader)/*?}*/)/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, PatPat.LOGGER::error)*//*?}*/.getFirst();
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to read config", e);
			if (CONFIG_FILE.length() > 0) { // TODO move it to client side too
				try {
					Path configPath = PatPatConfigManager.CONFIG_PATH;
					String backupFilename = FILENAME + ".bkp";
					int index = 0;
					while (configPath.resolve(backupFilename).toFile().exists()) {
						backupFilename = FILENAME + ++index + ".bkp";
					}
					Files.copy(CONFIG_FILE.toPath(), configPath.resolve(backupFilename));
				} catch (IOException ex) {
					PatPat.LOGGER.error("Failed to backup config", ex);
				}
			}
		}
		return PatPatServerConfig.create();
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		this.save(CONFIG_FILE);
	}

	public void save(File folder) {
		try (FileWriter writer = new FileWriter(folder, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(this));
			writer.write(json);
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to save config", e);
		}
	}
}
