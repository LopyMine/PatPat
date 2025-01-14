package net.lopymine.patpat.config.server;

import com.google.gson.*;
import lombok.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.utils.VersionedThings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
public class PatPatServerConfig {

	public static final Codec<PatPatServerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ListMode.CODEC.fieldOf("listMode").forGetter(PatPatServerConfig::getListMode),
			Codec.unboundedMap(VersionedThings.UUID_CODEC, Codec.STRING).fieldOf("list").forGetter(PatPatServerConfig::getList)
	).apply(instance, PatPatServerConfig::new));

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final Map<UUID, String> list;
	@Setter
	private ListMode listMode;

	private PatPatServerConfig() {
		this.listMode = ListMode.DISABLED;
		this.list     = new HashMap<>();
	}

	public PatPatServerConfig(ListMode listMode, Map<UUID, String> list) {
		this.listMode = listMode;
		this.list     = new HashMap<>(list);
	}

	public static PatPatServerConfig getInstance() {
		return PatPatServerConfig.read();
	}

	private static @NotNull PatPatServerConfig create() {
		PatPatServerConfig config = new PatPatServerConfig();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPat.LOGGER::error));*/
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
		}
		return PatPatServerConfig.create();
	}

	public void save() {
		CompletableFuture.runAsync(() -> {
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPat.LOGGER::error));*/
				writer.write(json);
			} catch (Exception e) {
				PatPat.LOGGER.error("Failed to save config", e);
			}
		});
	}
}
