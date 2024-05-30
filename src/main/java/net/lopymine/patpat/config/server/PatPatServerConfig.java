package net.lopymine.patpat.config.server;

import com.google.gson.*;
import lombok.*;
import net.minecraft.util.Uuids;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
public class PatPatServerConfig {
	public static final Codec<PatPatServerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListMode.CODEC.fieldOf("listMode").forGetter(PatPatServerConfig::getListMode),
			Codec.unboundedMap(Uuids.CODEC, Codec.STRING).xmap(HashMap::new, HashMap::new).fieldOf("list").forGetter(PatPatServerConfig::getPlayers)
	).apply(instance, PatPatServerConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger("PatPatServerConfig");
	private final HashMap<UUID, String> players;
	@Setter
	private ListMode listMode;

	private PatPatServerConfig() {
		this.listMode = ListMode.DISABLED;
		this.players = new HashMap<>();
	}

	public PatPatServerConfig(ListMode listMode, HashMap<UUID, String> players) {
		this.listMode = listMode;
		this.players = players;
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
		CompletableFuture.runAsync(() -> {
			String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).getOrThrow(false, LOGGER::error));
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				writer.write(json);
			} catch (Exception e) {
				LOGGER.error("Failed to save config", e);
			}
		});
	}
}
