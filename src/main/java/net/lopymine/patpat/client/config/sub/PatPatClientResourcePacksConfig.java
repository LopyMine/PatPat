package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.*;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientResourcePacksConfig {

	public static final Codec<PatPatClientResourcePacksConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("skipOldAnimationsEnabled", false, Codec.BOOL, PatPatClientResourcePacksConfig::isSkipOldAnimationsEnabled)
	).apply(instance, PatPatClientResourcePacksConfig::new));

	private boolean skipOldAnimationsEnabled;

	private PatPatClientResourcePacksConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientResourcePacksConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

}
