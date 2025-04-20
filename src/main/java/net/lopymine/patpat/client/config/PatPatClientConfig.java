package net.lopymine.patpat.client.config;

import com.google.gson.*;
import lombok.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.CodecUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@AllArgsConstructor
public class PatPatClientConfig {

	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("main", PatPatClientMainConfig.getNewInstance(), PatPatClientMainConfig.CODEC, PatPatClientConfig::getMainConfig),
			option("resourcePacks", PatPatClientResourcePacksConfig.getNewInstance(), PatPatClientResourcePacksConfig.CODEC, PatPatClientConfig::getResourcePacksConfig),
			option("sounds", PatPatClientSoundsConfig.getNewInstance(), PatPatClientSoundsConfig.CODEC, PatPatClientConfig::getSoundsConfig),
			option("visual", PatPatClientVisualConfig.getNewInstance(), PatPatClientVisualConfig.CODEC, PatPatClientConfig::getVisualConfig),
			option("server", PatPatClientServerConfig.getNewInstance(), PatPatClientServerConfig.CODEC, PatPatClientConfig::getServerConfig)
	).apply(instance, PatPatClientConfig::new));

	public static final PatPatClientConfig DEFAULT = new PatPatClientConfig();

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat-client.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private PatPatClientMainConfig mainConfig;
	private PatPatClientResourcePacksConfig resourcePacksConfig;
	private PatPatClientSoundsConfig soundsConfig;
	private PatPatClientVisualConfig visualConfig;
	private PatPatClientServerConfig serverConfig;

	private PatPatClientConfig() {
	}

	public static PatPatClientConfig getInstance() {
		PatPatClientConfig config = PatPatClientConfig.read();
		PatPatClient.LOGGER.setDebugMode(config.getMainConfig().isDebugLogEnabled());
		return config;
	}

	public static PatPatClientConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static @NotNull PatPatClientConfig create() {
		PatPatClientConfig config = PatPatClientConfig.getNewInstance();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPatClient.LOGGER::error));*/
			writer.write(json);
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static PatPatClientConfig read() {
		if (!CONFIG_FILE.exists()) {
			return PatPatClientConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, /*? <=1.17.1 {*//*new JsonParser().parse(reader)*//*?} else {*/JsonParser.parseReader(reader)/*?}*/)/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, PatPatClient.LOGGER::error)*//*?}*/.getFirst();
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to read config", e);
		}
		return PatPatClientConfig.create();
	}

	public void save() {
		PatPatClient.setConfig(this);
		CompletableFuture.runAsync(() -> {
			PatPatClient.LOGGER.debug("Saving PatPat Client Config...");
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPatClient.LOGGER::error));*/
				writer.write(json);
			} catch (Exception e) {
				PatPatClient.LOGGER.error("Failed to save config", e);
			}
			PatPatClient.LOGGER.setDebugMode(this.mainConfig.isDebugLogEnabled());
			PatPatClient.LOGGER.debug("Saved PatPat Client Config");
		});
	}
}
