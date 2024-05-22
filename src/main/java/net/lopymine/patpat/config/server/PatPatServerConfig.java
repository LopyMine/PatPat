package net.lopymine.patpat.config.server;

import com.google.gson.*;
import lombok.Getter;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.config.ListConfig;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

@Getter
public class PatPatServerConfig {
	public static final Codec<PatPatServerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfig.CODEC.fieldOf("whitelist").forGetter(PatPatServerConfig::getWhitelist),
			ListConfig.CODEC.fieldOf("blacklist").forGetter(PatPatServerConfig::getBlacklist)
	).apply(instance, PatPatServerConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat-server.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger("PatPatServerConfig");

	private final ListConfig whitelist;
	private final ListConfig blacklist;

	private PatPatServerConfig() {
		this.whitelist = ListConfig.empty();
		this.blacklist = ListConfig.empty();
	}

	private PatPatServerConfig(ListConfig whitelist, ListConfig blacklist) {
		this.whitelist = whitelist;
		this.blacklist = blacklist;
	}

	public static PatPatServerConfig getInstance() {
		return PatPatServerConfig.read();
	}

	private static @NotNull PatPatServerConfig create() {
		PatPatServerConfig config = new PatPatServerConfig();
		String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).getOrThrow(false, LOGGER::error));
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			writer.write(json);
		} catch (Exception e) {
			LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static PatPatServerConfig read() {
		if (!CONFIG_FILE.exists()) {
			return PatPatServerConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow(false, LOGGER::error).getFirst();
		} catch (Exception e) {
			LOGGER.error("Failed to read config", e);
		}
		return PatPatServerConfig.create();
	}

	public void save() {
		String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).getOrThrow(false, LOGGER::error));
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			writer.write(json);
		} catch (Exception e) {
			LOGGER.error("Failed to save config", e);
		}
	}

	public ListConfig getEnabledOne() {
		if (this.whitelist.isEnable() && !this.blacklist.isEnable()) {
			return this.whitelist;
		}
		if (this.blacklist.isEnable() && !this.whitelist.isEnable()) {
			return this.blacklist;
		}
		return this.whitelist;
	}
}
