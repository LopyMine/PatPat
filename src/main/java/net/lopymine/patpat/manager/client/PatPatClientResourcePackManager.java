package net.lopymine.patpat.manager.client;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import com.mojang.serialization.JsonOps;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.extension.EntityExtension;

import java.io.*;
import java.util.*;
import org.jetbrains.annotations.Nullable;

import static net.lopymine.patpat.PatPat.MOD_ID;

@ExtensionMethod(EntityExtension.class)
public class PatPatClientResourcePackManager {

	public static final PatPatClientResourcePackManager INSTANCE = new PatPatClientResourcePackManager();
	private final List<List<CustomAnimationConfig>> loadedAnimations = new ArrayList<>();

	private PatPatClientResourcePackManager() {
	}

	public static void parseConfig(String packName, Identifier identifier, InputSupplier<InputStream> inputStreamInputSupplier, List<CustomAnimationConfig> configs, PatPatClientConfig config) {
		String path = identifier.getPath();
		if (!path.endsWith(".json") && !path.endsWith(".json5")) {
			return;
		}
		try (InputStream inputStream = inputStreamInputSupplier.get(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			JsonElement json = JsonParser.parseReader(reader);
			if (!json.isJsonObject()) {
				PatPatClient.LOGGER.warn("ResourcePack '{}', file '{}' is not a json object, skip", packName, path);
				return;
			}

			JsonObject jsonObject = json.getAsJsonObject();
			JsonElement jsonElement = jsonObject.get("version");
			if (jsonElement == null) {
				PatPatClient.LOGGER.warn("ResourcePack '{}', file '{}' failed to verify version because it's missing, skip", packName, path);
				return;
			}

			String string = jsonElement.getAsString();
			Version configVersion = Version.of(string);
			if (configVersion.isLessThan(Version.DEFAULT)) {
				boolean shouldSkip = config.isSkipOldAnimationsEnabled();
				PatPatClient.LOGGER.warn("ResourcePack '{}', file '{}' has old version[{} > {}], {}!", packName, path, Version.DEFAULT, configVersion, (shouldSkip ? "skip" : "there may be errors"));
				if (shouldSkip) {
					return;
				}
			}
			CustomAnimationConfig animationConfig = CustomAnimationConfig.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, PatPatClient.LOGGER::error).getFirst();
			animationConfig.setConfigPath("%s/%s".formatted(packName, path));
			configs.add(animationConfig);
		} catch (Exception e) {
			PatPatClient.LOGGER.warn(String.format("ResourcePack '%s', file '%s' failed to parse, skip", packName, path), e);
		}
	}

	public void reload(List<ResourcePack> packs) {
		PatPatClientConfig config = PatPatClient.getConfig();
		this.loadedAnimations.clear();
		PatPatClientManager.reloadPatEntities();

		packs = packs.stream().filter(resourcePack -> resourcePack.getNamespaces(ResourceType.CLIENT_RESOURCES).contains(MOD_ID)).toList();
		List<List<CustomAnimationConfig>> serverResourcePacks = new ArrayList<>();
		List<List<CustomAnimationConfig>> resourcePacks = new ArrayList<>();

		for (ResourcePack pack : packs) {
			String resourcePackName = pack.getName();
			List<CustomAnimationConfig> animationConfigs = new ArrayList<>();

			PatPatClient.LOGGER.info("Registering {} resource pack", resourcePackName);
			pack.findResources(ResourceType.CLIENT_RESOURCES, MOD_ID, "textures", (id, input) -> parseConfig(resourcePackName, id, input, animationConfigs, config));

			if (animationConfigs.isEmpty()) {
				continue;
			}

			animationConfigs.sort(CustomAnimationConfig::compareTo);
			Collections.reverse(animationConfigs);

			if (config.isBypassServerResourcePackEnabled() && resourcePackName.startsWith("server/")) {
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