package net.lopymine.patpat.hand;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;

import net.lopymine.patpat.PatPat;

import java.util.HashMap;

public class PatPatSoundManager {
    public static final SoundEvent PATPAT_SOUND = PatPatSoundManager.registerSound("patpat");
    private static final HashMap<EntityType<?>, SoundEvent> PATPAT_SOUNDS = new HashMap<>(); // TODO

    public static void onInitialize() {
        PatPat.LOGGER.info("PatPat Sounds Manager Initialized");
    }

    static SoundEvent registerSound(String id) {
        return Registry.register(Registries.SOUND_EVENT, PatPat.i(id), SoundEvent.of(PatPat.i(id)));
    }

    public static void playSoundFor(Entity entity) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return;
        }
        world.playSoundFromEntity(entity, PatPatSoundManager.PATPAT_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
