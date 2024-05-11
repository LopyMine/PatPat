package net.lopymine.patpat.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.PatPatHandConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.lopymine.patpat.PatPat.LOGGER;
import static net.lopymine.patpat.PatPat.MOD_ID;

public class PatPatResourcePackManager {

	public static final PatPatResourcePackManager INSTANCE = new PatPatResourcePackManager();
	private final List<PatPatHandConfig> handConfigs = new ArrayList<>();
	private PatPatHandConfig overrideHandConfig = null;

	private PatPatResourcePackManager() {
	}

	public void reload(List<ResourcePack> packs) {
		this.handConfigs.clear();
		this.overrideHandConfig = null;
		PatPatClient.reloadPatEntities();

		for (ResourcePack pack : packs) {
			String name = pack.getName();
			if (!pack.getNamespaces(ResourceType.CLIENT_RESOURCES).contains(MOD_ID)
				|| name.equals(MOD_ID)) {
				continue;
			}

			LOGGER.info("Registering {} resource pack", name);
			pack.findResources(ResourceType.CLIENT_RESOURCES, MOD_ID, "textures", (id, input) -> this.findCustomHands(name, id, input));
		}
	}

	private void findCustomHands(String packName, Identifier identifier, InputSupplier<InputStream> inputStreamInputSupplier) {
		String path = identifier.getPath();
		if (path.endsWith(".json")) {
			try (InputStream inputStream = inputStreamInputSupplier.get(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				Optional<Pair<PatPatHandConfig, JsonElement>> pair = PatPatHandConfig.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).get().left();
				if (pair.isEmpty()) {
					LOGGER.warn("ResourcePack '{}', file '{}' failed to parse, skip", packName, path);
					return;
				}
				PatPatHandConfig config = pair.get().getFirst();
				if (config.isOverride()) {
					this.overrideHandConfig = config;
				}
				this.handConfigs.add(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Nullable
	public PatPatHandConfig getOverrideHandConfig() {
		return this.overrideHandConfig;
	}

	@Nullable
	public PatPatHandConfig getHandConfig(@NotNull Entity entity) {
		EntityType<?> entityType = entity.getType();
		for (PatPatHandConfig handConfig : this.handConfigs) {
			List<String> entities = handConfig.getEntities();
			if (entities.contains("all")) {
				return handConfig;
			}
			if (entities.contains(Registries.ENTITY_TYPE.getId(entityType).toString())) {
				return handConfig;
			}
		}
		return null;
	}
}
