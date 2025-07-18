package net.lopymine.patpat.client.resourcepack;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.serialization.JsonOps;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.extension.EntityExtension;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(EntityExtension.class)
public class PatPatClientResourcePackManager {

	public static final PatLogger LOGGER = PatPatClient.LOGGER.extend("ResourcePackManager");
	public static final PatPatClientResourcePackManager INSTANCE = new PatPatClientResourcePackManager();
	private final List<List<CustomAnimationConfig>> loadedAnimations = new ArrayList<>();

	private PatPatClientResourcePackManager() {
	}

	public static void parseConfig(String packName, ResourceLocation identifier, Supplier<InputStream> inputStreamInputSupplier, List<CustomAnimationConfig> configs, PatPatClientConfig config) {
		String path = identifier.getPath();
		if (!path.endsWith(".json") && !path.endsWith(".json5")) {
			return;
		}
		try (InputStream inputStream = inputStreamInputSupplier.get(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			JsonElement json = /*? <=1.17.1 {*//*new JsonParser().parse(reader)*//*?} else {*/JsonParser.parseReader(reader)/*?}*/;
			if (!json.isJsonObject()) {
				LOGGER.error("ResourcePack '{}', file '{}' is not a json object, skipping", packName, path);
				return;
			}

			JsonObject jsonObject = json.getAsJsonObject();
			JsonElement jsonElement = jsonObject.get("version");
			if (jsonElement == null) {
				LOGGER.error("ResourcePack '{}', file '{}' failed to get config version because it's missing, skipping", packName, path);
				return;
			}

			String string = jsonElement.getAsString();
			Version configVersion = Version.of(string);
			if (configVersion.isMoreThan(Version.CURRENT_MOD_VERSION)) {
				LOGGER.warn("ResourcePack '{}', file '{}' has unsupported new version[{} < {}], there may be errors!", packName, path, Version.CURRENT_MOD_VERSION, configVersion);
			} else if (configVersion.isLessThan(Version.RESOURCE_PACKS_MIN_SUPPORT_VERSION)) {
				boolean shouldSkip = config.getResourcePacksConfig().isSkipOldAnimationsEnabled();
				LOGGER.warn("ResourcePack '{}', file '{}' has unsupported old version[{} > {}], {}!", packName, path, Version.RESOURCE_PACKS_MIN_SUPPORT_VERSION, configVersion, (shouldSkip ? "skipping" : "there may be errors"));
				if (shouldSkip) {
					return;
				}
			}
			CustomAnimationConfig animationConfig = CustomAnimationConfig.CODEC.decode(JsonOps.INSTANCE, json)/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
			animationConfig.setConfigPath("%s/%s".formatted(packName, path));
			configs.add(animationConfig);
		} catch (Exception e) {
			LOGGER.warn(String.format("ResourcePack '%s', failed to parse file '%s', skipping", packName, path), e);
		}
	}

	public void reload(List<PackResources> reloadPacks, ResourceManager manager) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		this.loadedAnimations.clear();
		PatPatClientManager.clearPatEntities();
		List<PackResources> packs = reloadPacks.stream().filter(pack -> pack.getNamespaces(PackType.CLIENT_RESOURCES).contains(PatPat.MOD_ID)).toList();
		List<List<CustomAnimationConfig>> serverResourcePacks = new ArrayList<>();
		List<List<CustomAnimationConfig>> resourcePacks = new ArrayList<>();

		for (PackResources pack : packs) {
			String resourcePackName = /*? if >=1.19.3 {*/ pack.packId(); /*?} else {*//*pack.getName(); *//*?}*/
			List<CustomAnimationConfig> animationConfigs = new ArrayList<>();

			LOGGER.info("Registering {} resource pack", resourcePackName);
			//? >=1.19.3 {
			pack.listResources(PackType.CLIENT_RESOURCES, PatPat.MOD_ID, "textures", (id, input) -> {
				try (InputStream inputStream = input.get()) {
					parseConfig(resourcePackName, id, () -> inputStream, animationConfigs, config);
				} catch (Exception e) {
					LOGGER.error("Failed to read custom animation at {} from {}", id.toString(), resourcePackName);
				}
			});//?} else {
			/*Collection<ResourceLocation> customAnimationIds = pack.getResources(PackType.CLIENT_RESOURCES, PatPat.MOD_ID, "textures", /^? <=1.18.2 {^//^0,^//^?}^/ (identifier) -> {
				//? >=1.19 {
				return identifier.getPath().endsWith(".json") || identifier.getPath().endsWith(".json5");
				//?} else {
				/^return identifier.endsWith(".json") || identifier.endsWith(".json5");
				^///?}
			});
			for (ResourceLocation customAnimationId : customAnimationIds) {
				try (InputStream inputStream = /^? >=1.19 {^/manager.open(customAnimationId)/^?} else {^//^manager.getResource(customAnimationId).getInputStream()^//^?}^/) {
					PatPatClientResourcePackManager.parseConfig(resourcePackName, customAnimationId, () -> inputStream, animationConfigs, config);
				} catch (Exception e) {
					LOGGER.error("Failed to read custom animation at {} from {}", customAnimationId.toString(), resourcePackName);
				}
			}
			*///?}

			if (animationConfigs.isEmpty()) {
				continue;
			}

			animationConfigs.sort(CustomAnimationConfig::compareTo);
			Collections.reverse(animationConfigs);

			if (config.getMultiPlayerConfig().isBypassServerResourcePackPriorityEnabled() && resourcePackName.startsWith("server/")) {
				serverResourcePacks.add(animationConfigs);
			} else {
				resourcePacks.add(animationConfigs);
			}
		}
		this.loadedAnimations.addAll(Lists.reverse(resourcePacks));
		this.loadedAnimations.addAll(Lists.reverse(serverResourcePacks));
	}

	@Nullable
	public CustomAnimationConfig getAnimationConfig(LivingEntity entity, PlayerConfig whoPatted) {
		for (List<CustomAnimationConfig> configs : this.loadedAnimations) {
			for (CustomAnimationConfig config : configs) {
				if (config.canUseFor(entity, whoPatted)) {
					return config;
				}
			}
		}
		return null;
	}
}