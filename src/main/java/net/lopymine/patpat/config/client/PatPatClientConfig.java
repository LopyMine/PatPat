package net.lopymine.patpat.config.client;

import com.google.gson.*;
import lombok.*;
import net.minecraft.util.Uuids;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.manager.PatPatConfigManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PatPatClientConfig {
	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("bypassServerResourcePackPriorityEnabled").forGetter(PatPatClientConfig::isBypassServerResourcePackPriorityEnabled),
			Codec.BOOL.fieldOf("loweringAnimationEnabled").forGetter(PatPatClientConfig::isLoweringAnimationEnabled),
			Codec.BOOL.fieldOf("hidingNicknameEnabled").forGetter(PatPatClientConfig::isNicknameHidingEnabled),
			Codec.BOOL.fieldOf("swingHandEnabled").forGetter(PatPatClientConfig::isSwingHandEnabled),
			Codec.BOOL.fieldOf("soundsEnabled").forGetter(PatPatClientConfig::isSoundsEnabled),
			Codec.BOOL.fieldOf("patMeEnabled").forGetter(PatPatClientConfig::isPatMeEnabled),
			Codec.BOOL.fieldOf("modEnabled").forGetter(PatPatClientConfig::isModEnabled),
			Codec.DOUBLE.fieldOf("soundsVolume").forGetter(PatPatClientConfig::getSoundsVolume),
			ListMode.CODEC.fieldOf("listMode").forGetter(PatPatClientConfig::getListMode),
			Codec.unboundedMap(Uuids.CODEC, Codec.STRING).fieldOf("list").forGetter(PatPatClientConfig::getPlayers),
			OffsetsConfig.CODEC.fieldOf("animationOffsets").forGetter(PatPatClientConfig::getAnimationOffsets)
	).apply(instance, PatPatClientConfig::new));

	public static final PatPatClientConfig DEFAULT = new PatPatClientConfig();

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat-client.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger("PatPatClientConfig");
	private final Map<UUID, String> players;
	private final OffsetsConfig animationOffsets;
	private boolean bypassServerResourcePackPriorityEnabled;
	private boolean loweringAnimationEnabled;
	private boolean nicknameHidingEnabled;
	private boolean swingHandEnabled;
	private boolean soundsEnabled;
	private boolean patMeEnabled;
	private boolean modEnabled;
	private double soundsVolume;
	private ListMode listMode;

	public PatPatClientConfig() {
		this.bypassServerResourcePackPriorityEnabled = false;
		this.loweringAnimationEnabled = false;
		this.nicknameHidingEnabled = true;
		this.swingHandEnabled = true;
		this.soundsEnabled = true;
		this.patMeEnabled = true;
		this.modEnabled = true;
		this.soundsVolume = 1.0D;
		this.listMode = ListMode.DISABLED;
		this.players = new HashMap<>();
		this.animationOffsets = OffsetsConfig.EMPTY;
	}

	public PatPatClientConfig(boolean bypassServerResourcePackPriorityEnabled, boolean loweringAnimationEnabled, boolean nicknameHidingEnabled, boolean swingHandEnabled, boolean soundsEnabled, boolean patMeEnabled, boolean modEnabled, double soundsVolume, ListMode listMode, Map<UUID, String> players, OffsetsConfig animationOffsets) {
		this.bypassServerResourcePackPriorityEnabled = bypassServerResourcePackPriorityEnabled;
		this.loweringAnimationEnabled = loweringAnimationEnabled;
		this.nicknameHidingEnabled = nicknameHidingEnabled;
		this.swingHandEnabled = swingHandEnabled;
		this.soundsEnabled = soundsEnabled;
		this.patMeEnabled = patMeEnabled;
		this.modEnabled = modEnabled;
		this.soundsVolume = soundsVolume;
		this.listMode = listMode;
		this.players = new HashMap<>(players);
		this.animationOffsets = animationOffsets;
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
}
