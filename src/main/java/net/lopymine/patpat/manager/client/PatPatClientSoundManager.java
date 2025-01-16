package net.lopymine.patpat.manager.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.*;
import net.minecraft.util.math.MathHelper;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.resourcepack.SoundConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.utils.*;

public class PatPatClientSoundManager {

	private PatPatClientSoundManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		PatPatClientSoundManager.registerModSound("patpat");
		PatPatClientSoundManager.registerModSound("lopi");
	}

	private static void registerModSound(String id) {
		/*? >=1.19.3 {*/
		net.minecraft.registry.Registry
		/*?} else {*/
		/*net.minecraft.util.registry.Registry
		*//*?}*/.register(
				VersionedThings.SOUND_EVENT,
				IdentifierUtils.id(id),
				SoundUtils.getSoundEvent(id)
		);
	}

	public static void playSound(PatEntity patEntity, PlayerEntity player, double volume) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) {
			return;
		}
		SoundConfig soundConfig = patEntity.getAnimation().getSoundConfig();
		if (soundConfig == null) {
			PatPatClient.LOGGER.debug("SoundConfig not found in animation: {}", patEntity.getAnimation());
			return;
		}
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
