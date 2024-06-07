package net.lopymine.patpat.manager.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.MathHelper;

import net.lopymine.patpat.config.resourcepack.SoundConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.utils.IdentifierUtils;

public class PatPatClientSoundManager {

	private PatPatClientSoundManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		Registry.register(Registries.SOUND_EVENT, IdentifierUtils.id("patpat"), SoundEvent.of(IdentifierUtils.id("patpat")));
		Registry.register(Registries.SOUND_EVENT, IdentifierUtils.id("lopi"), SoundEvent.of(IdentifierUtils.id("lopi")));
	}

	public static void playSound(PatEntity patEntity, PlayerEntity player, double volume) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			return;
		}
		SoundConfig soundConfig = patEntity.getAnimation().soundConfig();
		SoundEvent soundEvent = soundConfig.getSound();
		world.playSound(player,
				patEntity.getEntity().getBlockPos(),
				soundEvent,
				SoundCategory.PLAYERS,
				soundConfig.getVolume() * (float) volume,
				MathHelper.nextFloat(
						world.random,
						soundConfig.getMinPitch(),
						soundConfig.getMaxPitch()
				));
	}
}
