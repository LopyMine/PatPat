package net.lopymine.patpat.config;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.lopymine.patpat.utils.IdentifierUtils;
import net.lopymine.patpat.utils.SoundUtils;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@Getter
public class SoundConfig {

	public static final Codec<SoundConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.xmap(SoundUtils::getSoundEvent, SoundUtils::getTypeId).fieldOf("id").forGetter(SoundConfig::getSound),
		Codec.FLOAT.fieldOf("min_pitch").forGetter(SoundConfig::getMinPitch),
		Codec.FLOAT.fieldOf("max_pitch").forGetter(SoundConfig::getMaxPitch)
	).apply(instance, SoundConfig::new));

	public static final Codec<SoundConfig> SOUND_FIELD = Codec.either(
		SoundConfig.CODEC, Codec.STRING).xmap(either -> {
		if (either.left().isPresent()) {
			return either.left().get();
		}
		if (either.right().isPresent()) {
			return new SoundConfig(either.right().get());
		}
		return null;
	}, soundConfig -> {
		if ((soundConfig.getMinPitch() == 1f) && (soundConfig.getMaxPitch() == 1f)) {
			return Either.right(soundConfig.getSound().getId().toString());
		}
		return Either.left(soundConfig);
	});

	private final SoundEvent sound;
	private final float minPitch;
	private final float maxPitch;


	public SoundConfig(SoundEvent soundEvent, float minPitch, float maxPitch) {
		this.sound = soundEvent;
		this.minPitch = minPitch;
		this.maxPitch = maxPitch;
	}

	public SoundConfig(SoundEvent sound) {
		this.sound = sound;
		this.minPitch = 0.96F;
		this.maxPitch = 1.03F;
	}

	public SoundConfig(String sound) {
		Identifier id = IdentifierUtils.id(sound);
		this.sound = SoundEvent.of(id);
		this.minPitch = 0.96F;
		this.maxPitch = 1.03F;
	}
}
