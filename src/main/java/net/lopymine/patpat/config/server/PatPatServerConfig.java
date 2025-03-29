package net.lopymine.patpat.config.server;

import com.google.gson.*;
import lombok.*;
import lombok.experimental.ExtensionMethod;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.extension.GsonExtension;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
@ExtensionMethod(GsonExtension.class)
public class PatPatServerConfig {

	public static final Codec<PatPatServerConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			Version.CODEC.fieldOf("version").forGetter(PatPatServerConfig::getVersion),
			ListMode.CODEC.fieldOf("listMode").forGetter(PatPatServerConfig::getListMode),
			RateLimitConfig.CODEC.fieldOf("rateLimit").forGetter(PatPatServerConfig::getRateLimitConfig)
	).apply(inst, PatPatServerConfig::new));

	private static final String FILENAME = "config.json";
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	@Getter
	private static PatPatServerConfig instance;
	@Getter
	private Version version;
	@Setter
	private ListMode listMode;
	private RateLimitConfig rateLimitConfig;

	private PatPatServerConfig() {
		this.listMode        = ListMode.DISABLED;
		this.rateLimitConfig = new RateLimitConfig();
		this.version         = Version.SERVER_CONFIG_VERSION;
	}

	public PatPatServerConfig(Version version, ListMode listMode, RateLimitConfig rateLimitConfig) {
		this.listMode        = listMode;
		this.rateLimitConfig = rateLimitConfig;
		this.version         = version;
	}

	public static void reload() {
		instance = PatPatServerConfig.read();
	}

	private static @NotNull PatPatServerConfig create() {
		PatPatServerConfig config = new PatPatServerConfig();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			// String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPat.LOGGER::error));*/
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
			if (CONFIG_FILE.length() > 0) {
				try {
					Path configPath = PatPatConfigManager.CONFIG_PATH;
					String backupFilename = FILENAME + ".bkp";
					int index = 0;
					while (configPath.resolve(backupFilename).toFile().exists()) {
						backupFilename = FILENAME + String.valueOf(++index) + ".bkp";
					}
					Files.copy(CONFIG_FILE.toPath(), configPath.resolve(backupFilename));
				} catch (IOException ex) {
					PatPat.LOGGER.error("Failed to backup config", ex);
				}
			}

		}
		return PatPatServerConfig.create();
	}

	public void save() {
		CompletableFuture.runAsync(this::saveSync);
	}

	public void saveSync() {
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			// String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPat.LOGGER::error));*/
			String json = GSON.toJson(CODEC.encode(this));
			writer.write(json);
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to save config", e);
		}
	}
}
