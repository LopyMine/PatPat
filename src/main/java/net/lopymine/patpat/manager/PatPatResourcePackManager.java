package net.lopymine.patpat.manager;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.resource.*;

import com.mojang.serialization.JsonOps;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.PatPatHandConfig;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.Nullable;

import static net.lopymine.patpat.PatPat.LOGGER;
import static net.lopymine.patpat.PatPat.MOD_ID;

public class PatPatResourcePackManager {

    public static final PatPatResourcePackManager INSTANCE = new PatPatResourcePackManager();

    private PatPatResourcePackManager(){
    }

    private final Set<PatPatHandConfig> handConfigs = new HashSet<>();
    private PatPatHandConfig overrideHandConfig = null;

    public void load(List<ResourcePack> packs) {
        this.handConfigs.clear();
        this.overrideHandConfig = null;

        for (ResourcePack pack : packs) {
            if (pack.getName().equals(MOD_ID)) {
                continue;
            }
            Set<String> namespaces = pack.getNamespaces(ResourceType.CLIENT_RESOURCES);

            for (String name : namespaces) {
                if (!name.equals(PatPat.MOD_ID)) {
                    continue;
                }
                System.out.println(name + " from " + pack.getName());
                LOGGER.info("Registering {} resource pack to override PatPat mod.", pack.getName());

                ResourcePack.ResultConsumer resultConsumer = (identifier, inputStreamInputSupplier) -> {
                    String path = identifier.getPath();
                    System.out.println(path);
                    if (path.endsWith("custom_hand.json")) {
                        try (InputStream inputStream = inputStreamInputSupplier.get(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            PatPatHandConfig config = PatPatHandConfig.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow(false, LOGGER::error).getFirst();
                            if (config.override()) {
                                this.overrideHandConfig = config;
                                System.out.println(config);
                            }
                            this.handConfigs.add(config);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                pack.findResources(ResourceType.CLIENT_RESOURCES, name, "textures", resultConsumer);
            }
        }
    }

    @Nullable
    public PatPatHandConfig getOverrideHandConfig() {
        return this.overrideHandConfig;
    }

    @Nullable
    public PatPatHandConfig getFor(Entity entity) {
        for (PatPatHandConfig handConfig : handConfigs) {
            for (String entityId : handConfig.entities()) {
                if (entityId.equals(Registries.ENTITY_TYPE.getId(entity.getType()).toString())) {
                    return handConfig;
                }
            }
        }
        return null;
    }
}
