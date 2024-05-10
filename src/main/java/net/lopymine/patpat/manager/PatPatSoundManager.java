package net.lopymine.patpat.manager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.MathHelper;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entity.PatEntity;

public class PatPatSoundManager {

    public static void onInitialize() {
        PatPat.LOGGER.info("PatPat Sounds Initialized");
        Registry.register(Registries.SOUND_EVENT, PatPat.i("patpat"), SoundEvent.of(PatPat.i("patpat")));
        Registry.register(Registries.SOUND_EVENT, PatPat.i("lopi"), SoundEvent.of(PatPat.i("lopi")));
    }

    public static void playSoundFor(PatEntity patEntity, PlayerEntity player) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return;
        }
        SoundEvent soundEvent = SoundEvent.of(PatPat.i(patEntity.getAnimation().getSounds()));
        world.playSoundFromEntity(player, soundEvent, SoundCategory.PLAYERS, 1.0F, MathHelper.nextFloat(world.random, 0.96F, 1.03F));
    }
}
