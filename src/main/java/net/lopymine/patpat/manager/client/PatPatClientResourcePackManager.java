package net.lopymine.patpat.manager.client;

import com.google.gson.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
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
	private final LinkedList<List<CustomAnimationConfig>> loadedAnimations = new LinkedList<>();

	public static void parseConfig(String packName, Identifier identifier, InputSupplier<InputStream> inputStreamInputSupplier, List<CustomAnimationConfig> configs) {
		String path = identifier.getPath();
		if (!path.endsWith(".json") && !path.endsWith(".json5")) {
			return;
		}
		try (InputStream inputStream = inputStreamInputSupplier.get(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			JsonElement json = JsonParser.parseReader(reader);

			if (!json.isJsonObject()) {
				return;
			}

			JsonObject jsonObject = json.getAsJsonObject();
			JsonElement jsonElement = jsonObject.get("version");
			if (jsonElement != null) {
				String string = jsonElement.getAsString();
				Version configVersion = Version.of(string);
				if (configVersion.isLessThan(Version.DEFAULT)) {
					PatPatClient.LOGGER.warn("ResourcePack '{}', file '{}' has old version[{} > {}], there may be errors!", packName, path, Version.DEFAULT, configVersion);
				}
			} else {
				PatPatClient.LOGGER.warn("ResourcePack '{}', file '{}' failed to verify version because it's missing, skip", packName, path);
				return;
			}

			CustomAnimationConfig config = CustomAnimationConfig.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, PatPatClient.LOGGER::error).getFirst();
			configs.add(config);
		} catch (Exception e) {
			PatPatClient.LOGGER.warn(String.format("ResourcePack '%s', file '%s' failed to parse, skip", packName, path), e);
		}
	}

	public void reload(List<ResourcePack> packs) {
		PatPatClientConfig config = PatPatClient.getConfig();
		this.loadedAnimations.clear();
		PatPatClientManager.reloadPatEntities();

		LinkedList<List<CustomAnimationConfig>> bypassedLoadedAnimations = new LinkedList<>();
		// TODO
		//  Этот код нужно переписать/дописать т.к. сейчас он работает не совсем правильно
		//  Сейчас есть проблема в сортировке ресурспаков, так как:
		//  В packs ресурспаки сортируется вот так: 1)Моды 2)Модовые ресурспаки 3)Встроенные ресурспаки 4)Пользовательские ресурспаки (вроде так)
		//  Также пользовательские ресурспаки с наибольшим приоритетом в игре находятся в самом конце списка,
		//  поэтому цикл снизу идёт с конца, но в таком варианте есть ещё другая проблема:
		//  В таком варианте трудно переместить серверный ресурспак, ведь если ставить его в самый конец, то выйдет что
		//  он будет по приоритету ниже модовых (самые первые в списке)

		for (int i = packs.size() - 1; i > 0; i--) {
			ResourcePack pack = packs.get(i);
//			System.out.println(pack.getClass());
			String resourcePackName = pack.getName();
//			System.out.println(resourcePackName);
			if (!pack.getNamespaces(ResourceType.CLIENT_RESOURCES).contains(MOD_ID)) {
				continue;
			}

			List<CustomAnimationConfig> animationConfigs = new ArrayList<>();
			PatPatClient.LOGGER.info("Registering {} resource pack", resourcePackName);
			pack.findResources(ResourceType.CLIENT_RESOURCES, MOD_ID, "textures", (id, input) -> parseConfig(resourcePackName, id, input, animationConfigs));

			if (animationConfigs.isEmpty()) {
				continue;
			}

			animationConfigs.sort((Comparator.naturalOrder()));

			ResourcePackProfile profile = MinecraftClient.getInstance().getResourcePackManager().getProfile(resourcePackName);
			boolean addLater = (profile != null && profile.isPinned() && config.isBypassServerResourcePackPriorityEnabled()) || resourcePackName.equals(MOD_ID);
			if (addLater) {
				bypassedLoadedAnimations.add(animationConfigs);
			} else {
				this.loadedAnimations.add(animationConfigs);
			}
		}
		this.loadedAnimations.addAll(bypassedLoadedAnimations);
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
