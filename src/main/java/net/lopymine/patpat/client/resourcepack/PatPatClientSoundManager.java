package net.lopymine.patpat.client.resourcepack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.resourcepack.SoundConfig;
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
		net.minecraft.core.Registry.register(
				VersionedThings.SOUND_EVENT,
				IdentifierUtils.modId(id),
				SoundUtils.getSoundEvent(id)
		);
	}

	public static void playSound(PatEntity patEntity, Player player, double volume) {
		ClientLevel world = Minecraft.getInstance().level;
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
				patEntity.getEntity().blockPosition(),
				soundEvent,
				SoundSource.PLAYERS,
				soundConfig.getVolume() * (float) volume,
				Mth.nextFloat(
						world.random,
						soundConfig.getMinPitch(),
						soundConfig.getMaxPitch()
				));
	}
}
