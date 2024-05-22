package net.lopymine.patpat.config.client;

import com.google.gson.*;
import lombok.Getter;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.config.ListConfig;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
public class PatPatClientConfig {
	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListConfig.CODEC.fieldOf("whitelist").forGetter(PatPatClientConfig::getWhitelist),
			ListConfig.CODEC.fieldOf("blacklist").forGetter(PatPatClientConfig::getBlacklist)
	).apply(instance, PatPatClientConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat-сlient.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger("PatPatClientConfig");

	private final ListConfig whitelist;
	private final ListConfig blacklist;

	private PatPatClientConfig() {
		this.whitelist = ListConfig.empty();
		this.whitelist.setEnable(true);
		this.blacklist = ListConfig.empty();
	}

	private PatPatClientConfig(ListConfig whitelist, ListConfig blacklist) {
		this.whitelist = whitelist;
		this.blacklist = blacklist;
	}

	public static PatPatClientConfig getInstance() {
		return PatPatClientConfig.read();
	}

	private static @NotNull PatPatClientConfig create() {
		PatPatClientConfig config = new PatPatClientConfig();
		String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).getOrThrow(false, LOGGER::error));
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			writer.write(json);
		} catch (Exception e) {
			LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static PatPatClientConfig read() {
		if (!CONFIG_FILE.exists()) {
			return PatPatClientConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow(false, LOGGER::error).getFirst();
		} catch (Exception e) {
			LOGGER.error("Failed to read config", e);
		}
		return PatPatClientConfig.create();
	}

	public void save() {
		CompletableFuture.runAsync(() -> {
			String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).getOrThrow(false, LOGGER::error));
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				writer.write(json);
			} catch (Exception e) {
				LOGGER.error("Failed to save config", e);
			}
		});
	}

	public boolean containsInEnabledList(UUID playerUuid) { // TODO Додумать проверку
		if (this.whitelist.isEnable() && !this.blacklist.isEnable()) {
			return this.whitelist.getPlayers().containsKey(playerUuid);
		}
		if (this.blacklist.isEnable() && !this.whitelist.isEnable()) {
			return this.blacklist.getPlayers().containsKey(playerUuid);
		}
		return false;
	}
}
