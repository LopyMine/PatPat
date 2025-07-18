package net.lopymine.patpat.client.resourcepack;

import net.lopymine.patpat.PatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.resourcepack.SoundConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.utils.*;

public class PatPatClientSoundManager {

	public static final PatLogger LOGGER = PatPatClient.LOGGER.extend("SoundManager");

	private PatPatClientSoundManager() {
		throw new IllegalStateException("Manager class");
	}

	private static SoundEvent patPatSoundEvent;
	private static SoundEvent lopiSoundEvent;

	public static void register() {
		lopiSoundEvent   = getLopiSoundEvent();
		patPatSoundEvent = getPatPatSoundEvent();
	}

	public static SoundEvent getLopiSoundEvent() {
		if (lopiSoundEvent == null) {
			lopiSoundEvent = registerModSound("lopi");
		}
		return lopiSoundEvent;
	}

	public static SoundEvent getPatPatSoundEvent() {
		if (patPatSoundEvent == null) {
			patPatSoundEvent = registerModSound("patpat");
		}
		return patPatSoundEvent;
	}

	private static SoundEvent registerModSound(String id) {
		return Registry.register(
				VersionedThings.SOUND_EVENT,
				IdentifierUtils.modId(id),
				SoundUtils.getSoundEvent(id)
		);
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
		SoundEvent soundEvent = soundConfig.getSound();
		world.playSound(pattedEntity,
				whoPatted.getEntity().blockPosition(),
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
