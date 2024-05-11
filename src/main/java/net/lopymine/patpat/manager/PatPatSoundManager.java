package net.lopymine.patpat.manager;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.utils.IdentifierUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class PatPatSoundManager {

	public static void onInitialize() {
		Registry.register(Registries.SOUND_EVENT, IdentifierUtils.id("patpat"), SoundEvent.of(IdentifierUtils.id("patpat")));
		Registry.register(Registries.SOUND_EVENT, IdentifierUtils.id("lopi"), SoundEvent.of(IdentifierUtils.id("lopi")));
		PatPat.LOGGER.info("PatPat Sounds Initialized");
	}

	public static void playSound(PatEntity patEntity, PlayerEntity player) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			return;
		}
		SoundEvent soundEvent = patEntity.getAnimation().getSound();
		world.playSoundFromEntity(player, soundEvent, SoundCategory.PLAYERS, 1.0F, MathHelper.nextFloat(world.random, 0.96F, 1.03F));
	}
}
