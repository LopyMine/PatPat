package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.CodecUtils;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientSoundsConfig {

	public static final PatPatClientSoundsConfig DEFAULT = new PatPatClientSoundsConfig(
			true,
			1.0F
	);

	public static final Codec<PatPatClientSoundsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("soundsEnabled", DEFAULT.soundsEnabled, Codec.BOOL, PatPatClientSoundsConfig::isSoundsEnabled),
			option("soundsVolume", DEFAULT.soundsVolume, Codec.FLOAT, PatPatClientSoundsConfig::getSoundsVolume)
	).apply(instance, PatPatClientSoundsConfig::new));

	private boolean soundsEnabled;
	private float soundsVolume;

	public static PatPatClientSoundsConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public PatPatClientSoundsConfig copy() {
		return new PatPatClientSoundsConfig(
				this.soundsEnabled,
				this.soundsVolume
		);
	}

}
