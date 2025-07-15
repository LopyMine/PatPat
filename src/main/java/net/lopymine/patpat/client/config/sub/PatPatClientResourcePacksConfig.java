package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.utils.CodecUtils;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientResourcePacksConfig {

	public static final PatPatClientResourcePacksConfig DEFAULT = new PatPatClientResourcePacksConfig(
			false
	);

	public static final Codec<PatPatClientResourcePacksConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("skipOldAnimationsEnabled", DEFAULT.skipOldAnimationsEnabled, Codec.BOOL, PatPatClientResourcePacksConfig::isSkipOldAnimationsEnabled)
	).apply(instance, PatPatClientResourcePacksConfig::new));

	private boolean skipOldAnimationsEnabled;

	public static PatPatClientResourcePacksConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public PatPatClientResourcePacksConfig copy() {
		return new PatPatClientResourcePacksConfig(
				this.skipOldAnimationsEnabled
		);
	}

}
