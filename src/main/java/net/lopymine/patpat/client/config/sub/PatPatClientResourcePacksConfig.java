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
public class PatPatClientResourcePacksConfig {

	public static final Codec<PatPatClientResourcePacksConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("skipOldAnimationsEnabled", false, Codec.BOOL, PatPatClientResourcePacksConfig::isSkipOldAnimationsEnabled)
	).apply(instance, PatPatClientResourcePacksConfig::new));

	private boolean skipOldAnimationsEnabled;

	public static Supplier<PatPatClientResourcePacksConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

}
