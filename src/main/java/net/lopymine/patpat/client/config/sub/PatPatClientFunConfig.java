package net.lopymine.patpat.client.config.sub;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import lombok.*;
import net.lopymine.patpat.utils.CodecUtils;
import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientFunConfig {

	public static final Codec<PatPatClientFunConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("pvpModeEnabled", false, Codec.BOOL, PatPatClientFunConfig::isPvpModeEnabled)
	).apply(instance, PatPatClientFunConfig::new));

	private boolean pvpModeEnabled;

	public static Supplier<PatPatClientFunConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public enum PvpMode { // todo

		DISABLED,
		ONLY_IF_EMPTY_HAND,
		ALWAYS

	}

}
