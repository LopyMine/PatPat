package net.lopymine.patpat.client.config.resourcepack;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.utils.SoundUtils;
import net.minecraft.sounds.SoundEvent;

public record SoundConfig(SoundEvent sound, float minPitch, float maxPitch, float volume) {

	public static final SoundConfig PAT_PAT_SOUND = new SoundConfig(PatPatClientSoundManager.getPatPatSoundEvent(), 1.0F, 1.0F, 1.0F);

	public static final Codec<SoundConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.xmap(SoundUtils::getSoundEvent, SoundUtils::getLocation).fieldOf("id").forGetter(SoundConfig::sound),
			Codec.FLOAT.optionalFieldOf("min_pitch", -1.0F).forGetter(SoundConfig::minPitch),
			Codec.FLOAT.optionalFieldOf("max_pitch", -1.0F).forGetter(SoundConfig::maxPitch),
			Codec.FLOAT.optionalFieldOf("minPitch", -1.0F).forGetter(SoundConfig::minPitch),
			Codec.FLOAT.optionalFieldOf("maxPitch", -1.0F).forGetter(SoundConfig::maxPitch),
			Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(SoundConfig::volume)
	).apply(instance, (event, minPitch, maxPitch, minPitchTwo, maxPitchTwo, volume) -> {
		float minP = minPitch == -1.0F ?
				minPitchTwo == -1.0F ?
						1.0F
						:
						minPitchTwo
				:
				minPitch;
		float maxP = maxPitch == -1.0F ?
				maxPitchTwo == -1.0F ?
						1.0F
						:
						maxPitchTwo
				:
				maxPitch;
		return new SoundConfig(event, minP, maxP, volume);
	}));

	public static final Codec<SoundConfig> STRINGED_CODEC = Codec.either(SoundConfig.CODEC, Codec.STRING).xmap(either -> {
		if (either.left().isPresent()) {
			return either.left().get();
		}
		if (either.right().isPresent()) {
			return new SoundConfig(either.right().get());
		}
		return null;
	}, soundConfig -> {
		if ((soundConfig.minPitch() == 1.0F) && (soundConfig.maxPitch() == 1.0F) && (soundConfig.volume() == 1.0F)) {
			return Either.right(SoundUtils.getLocation(soundConfig.sound()));
		}
		return Either.left(soundConfig);
	});

	public SoundConfig(String sound) {
		this(SoundUtils.getSoundEvent(sound), 1.0F, 1.0F, 1.0F);
	}

	@Override
	public String toString() {
		return "SoundConfig{" +
				"sound=" + SoundUtils.getLocation(this.sound) +
				", minPitch=" + this.minPitch +
				", maxPitch=" + this.maxPitch +
				", volume=" + this.volume +
				'}';
	}
}
