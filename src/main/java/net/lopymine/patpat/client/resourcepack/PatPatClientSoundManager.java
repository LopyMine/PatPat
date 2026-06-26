package net.lopymine.patpat.client.resourcepack;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.resourcepack.SoundConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.lopymine.patpat.logger.PatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PatPatClientSoundManager {

	public static final PatLogger LOGGER = PatPatClient.LOGGER.extend("SoundManager");
	private static SoundEvent patPatSoundEvent;
	private static SoundEvent lopiSoundEvent;
	private PatPatClientSoundManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ClientMultiLoader.getInstance().registerSounds((register) -> {
			lopiSoundEvent   = register.registerSound("lopi");
			patPatSoundEvent = register.registerSound("patpat");
		});
	}

	public static SoundEvent getLopiSoundEvent() {
		if (lopiSoundEvent == null) {
			throw new IllegalStateException();
		}
		return lopiSoundEvent;
	}

	public static SoundEvent getPatPatSoundEvent() {
		if (patPatSoundEvent == null) {
			throw new IllegalStateException();
		}
		return patPatSoundEvent;
	}

	public static void playSound(PatEntity whoPatted, Player pattedEntity, double volume) {
		ClientLevel world = Minecraft.getInstance().level;
		if (world == null) {
			return;
		}
		SoundConfig soundConfig = whoPatted.getAnimation().getSoundConfig();
		if (soundConfig == null) {
			LOGGER.debug("Failed to find sound config in animation: {}", whoPatted.getAnimation());
			return;
		}
		LOGGER.debug("Playing sound from config {}, whoPatted: {}, pattedEntity: {}", soundConfig.toString(), whoPatted.toString(), pattedEntity.toString());
		SoundEvent soundEvent = soundConfig.sound();
		world.playSound(pattedEntity,
				whoPatted.getEntity().blockPosition(),
				soundEvent,
				SoundSource.PLAYERS,
				soundConfig.volume() * (float) volume,
				Mth.nextFloat(
						world.getRandom(),
						soundConfig.minPitch(),
						soundConfig.maxPitch()
				));
	}
}
