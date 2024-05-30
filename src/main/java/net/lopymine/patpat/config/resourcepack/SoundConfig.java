package net.lopymine.patpat.config.resourcepack;

import lombok.Getter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.*;

@Getter
public class SoundConfig {
	public static final SoundConfig PATPAT_SOUND = new SoundConfig("patpat");
	public static final SoundConfig LOPI_SOUND = new SoundConfig("lopi");

	public static final Codec<SoundConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.xmap(SoundUtils::getSoundEvent, SoundUtils::getTypeId).fieldOf("id").forGetter(SoundConfig::getSound),
			Codec.FLOAT.optionalFieldOf("minPitch", 1.0F).forGetter(SoundConfig::getMinPitch),
			Codec.FLOAT.optionalFieldOf("maxPitch", 1.0F).forGetter(SoundConfig::getMaxPitch),
			Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(SoundConfig::getVolume)
	).apply(instance, SoundConfig::new));

	public static final Codec<SoundConfig> STRINGED_CODEC = Codec.either(SoundConfig.CODEC, Codec.STRING).xmap(either -> {
		if (either.left().isPresent()) {
			return either.left().get();
		}
		if (either.right().isPresent()) {
			return new SoundConfig(either.right().get());
		}
		return null;
	}, soundConfig -> {
		if ((soundConfig.getMinPitch() == 1.0F) && (soundConfig.getMaxPitch() == 1.0F) && (soundConfig.getVolume() == 1.0F)) {
			return Either.right(soundConfig.getSound().getId().toString());
		}
		return Either.left(soundConfig);
	});

	private final SoundEvent sound;
	private final float minPitch;
	private final float maxPitch;
	private final float volume;

	public SoundConfig(SoundEvent soundEvent, float minPitch, float maxPitch, float volume) {
		this.sound = soundEvent;
		this.minPitch = minPitch;
		this.maxPitch = maxPitch;
		this.volume = volume;
	}

	public SoundConfig(String sound) {
		Identifier id = IdentifierUtils.id(sound);
		this.sound = SoundEvent.of(id);
		this.minPitch = 1.0F;
		this.maxPitch = 1.0F;
		this.volume = 1.0F;
	}
}