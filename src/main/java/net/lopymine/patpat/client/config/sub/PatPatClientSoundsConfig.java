package net.lopymine.patpat.client.config.sub;

import java.util.function.Supplier;
import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.CodecUtils;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientSoundsConfig {

	public static final Codec<PatPatClientSoundsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("soundsEnabled", true, Codec.BOOL, PatPatClientSoundsConfig::isSoundsEnabled),
			option("soundsVolume", 1.0F, Codec.FLOAT, PatPatClientSoundsConfig::getSoundsVolume)
	).apply(instance, PatPatClientSoundsConfig::new));

	private boolean soundsEnabled;
	private float soundsVolume;

	public static Supplier<PatPatClientSoundsConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

}
