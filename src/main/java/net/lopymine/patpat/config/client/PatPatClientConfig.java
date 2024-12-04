package net.lopymine.patpat.config.client;

import com.google.gson.*;
import lombok.*;
import net.minecraft.client.MinecraftClient;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.utils.VersionedThings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientConfig {

	public static final Codec<PatPatClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(VersionedThings.UUID_CODEC, Codec.STRING).xmap(HashMap::new, HashMap::new).fieldOf("list").forGetter(PatPatClientConfig::getPlayers),
			Codec.BOOL.fieldOf("bypassServerResourcePackPriorityEnabled").forGetter(PatPatClientConfig::isBypassServerResourcePackEnabled),
			Codec.BOOL.fieldOf("hidingNicknameEnabled").forGetter(PatPatClientConfig::isNicknameHidingEnabled),
			Codec.BOOL.fieldOf("swingHandEnabled").forGetter(PatPatClientConfig::isSwingHandEnabled),
			Codec.BOOL.fieldOf("soundsEnabled").forGetter(PatPatClientConfig::isSoundsEnabled),
			Codec.BOOL.fieldOf("patMeEnabled").forGetter(PatPatClientConfig::isPatMeEnabled),
			Codec.BOOL.fieldOf("cameraShackingEnabled").forGetter(PatPatClientConfig::isCameraShackingEnabled),
			Codec.BOOL.fieldOf("modEnabled").forGetter(PatPatClientConfig::isModEnabled),
			Codec.FLOAT.fieldOf("soundsVolume").forGetter(PatPatClientConfig::getSoundsVolume),
			ListMode.CODEC.fieldOf("listMode").forGetter(PatPatClientConfig::getListMode),
			Codec.FLOAT.fieldOf("offsetX").forGetter(PatPatClientConfig::getAnimationOffsetX),
			Codec.FLOAT.fieldOf("offsetY").forGetter(PatPatClientConfig::getAnimationOffsetY),
			Codec.FLOAT.fieldOf("offsetZ").forGetter(PatPatClientConfig::getAnimationOffsetZ),
			Codec.BOOL.fieldOf("debugLogEnabled").forGetter(PatPatClientConfig::isDebugLogEnabled),
			Codec.BOOL.fieldOf("skipOldAnimationsEnabled").forGetter(PatPatClientConfig::isSkipOldAnimationsEnabled),
			Codec.FLOAT.optionalFieldOf("patWeight").xmap(o -> o.orElse(0.425F), Optional::ofNullable).forGetter(PatPatClientConfig::getPatWeight)
	).apply(instance, PatPatClientConfig::new));

	public static final PatPatClientConfig DEFAULT = new PatPatClientConfig();

	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("patpat-client.json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final HashMap<UUID, String> players;
	private boolean bypassServerResourcePackEnabled;
	private boolean nicknameHidingEnabled;
	private boolean swingHandEnabled;
	private boolean soundsEnabled;
	private boolean patMeEnabled;
	private boolean cameraShackingEnabled;
	private boolean modEnabled;
	private float soundsVolume;
	private ListMode listMode;
	private float animationOffsetX;
	private float animationOffsetY;
	private float animationOffsetZ;
	private boolean debugLogEnabled;
	private boolean skipOldAnimationsEnabled;
	private float patWeight;

	public PatPatClientConfig() {
		this.bypassServerResourcePackEnabled = false;
		this.nicknameHidingEnabled           = true;
		this.swingHandEnabled                = true;
		this.soundsEnabled                   = true;
		this.patMeEnabled                    = true;
		this.cameraShackingEnabled           = true;
		this.modEnabled                      = true;
		this.soundsVolume                    = 1.0F;
		this.listMode                        = ListMode.DISABLED;
		this.players                         = new HashMap<>();
		this.animationOffsetX                = 0.0F;
		this.animationOffsetY                = 0.0F;
		this.animationOffsetZ                = 0.0F;
		this.debugLogEnabled                 = false;
		this.skipOldAnimationsEnabled        = true;
		this.patWeight                       = 0.425F;
	}

	public static PatPatClientConfig getInstance() {
		return PatPatClientConfig.read();
	}

	private static @NotNull PatPatClientConfig create() {
		PatPatClientConfig config = new PatPatClientConfig();
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
			if (PatPatClient.getConfig().isDebugLogEnabled()) {
				PatPatClient.LOGGER.info("Saving PatPat Client Config...");
			}
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, PatPatClient.LOGGER::error));*/
				writer.write(json);
			} catch (Exception e) {
				PatPatClient.LOGGER.error("Failed to save config", e);
			}
			if (PatPatClient.getConfig().isDebugLogEnabled()) {
				PatPatClient.LOGGER.info("Saved PatPat Client Config");
			}
		});
	}

	public void setBypassServerResourcePackEnabled(boolean value) {
		this.bypassServerResourcePackEnabled = value;
		MinecraftClient client = MinecraftClient.getInstance();
		boolean bl = client.getResourcePackManager()
				/*? <=1.20.4 {*//*.getEnabledNames()
				 *//*?} else {*/.getEnabledIds()/*?}*/
				.stream().anyMatch(s -> s.startsWith("server/"));

		if (bl) {
			client.reloadResources();
		}
	}
}
