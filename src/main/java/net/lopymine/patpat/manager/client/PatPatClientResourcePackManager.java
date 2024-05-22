package net.lopymine.patpat.manager.client;

import com.google.gson.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.entity.Entity;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import com.mojang.serialization.JsonOps;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.resourcepack.PatPatHandConfig;
import net.lopymine.patpat.extension.EntityExtension;

import java.io.*;
import java.util.*;
import org.jetbrains.annotations.*;

import static net.lopymine.patpat.PatPat.MOD_ID;

@ExtensionMethod(EntityExtension.class)
public class PatPatClientResourcePackManager {

	public static final PatPatClientResourcePackManager INSTANCE = new PatPatClientResourcePackManager();
	private final List<PatPatHandConfig> handConfigs = new ArrayList<>();
	private PatPatHandConfig overrideHandConfig = null;

	private PatPatClientResourcePackManager() {
	}

	public void reload(List<ResourcePack> packs) {
		this.handConfigs.clear();
		this.overrideHandConfig = null;
		PatPatClientManager.reloadPatEntities();

		for (ResourcePack pack : packs) {
			String name = pack.getName();
			if (!pack.getNamespaces(ResourceType.CLIENT_RESOURCES).contains(MOD_ID)
				|| name.equals(MOD_ID)) {
				continue;
			}

			PatPatClient.LOGGER.info("Registering {} resource pack", name);
			pack.findResources(ResourceType.CLIENT_RESOURCES, MOD_ID, "textures", (id, input) -> this.findCustomHands(name, id, input));
		}
	}

	private void findCustomHands(String packName, Identifier identifier, InputSupplier<InputStream> inputStreamInputSupplier) {
		String path = identifier.getPath();
		if (path.endsWith(".json")) {
			try (InputStream inputStream = inputStreamInputSupplier.get(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				JsonElement json = JsonParser.parseReader(reader);
				PatPatHandConfig config = PatPatHandConfig.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, (s) -> {
				}).getFirst();
				if (config.isOverride()) {
					this.overrideHandConfig = config;
				}
				this.handConfigs.add(config);
			} catch (Exception e) {
				PatPatClient.LOGGER.warn(String.format("ResourcePack '%s', file '%s' failed to parse, skip", packName, path), e);
			}
		}
	}

	@Nullable
	public PatPatHandConfig getOverrideHandConfig() {
		return this.overrideHandConfig;
	}

	@Nullable
	public PatPatHandConfig getHandConfig(@NotNull Entity entity) {
		for (PatPatHandConfig handConfig : this.handConfigs) {
			if (handConfig.isApply(entity)) {
				return handConfig;
			}
		}
		return null;
	}
}
