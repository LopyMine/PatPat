package net.lopymine.patpat.client.config.sub;

import java.util.function.Supplier;
import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.packet.PatPatClientProxLibManager;
import net.lopymine.patpat.utils.CodecUtils;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientProximityPacketsConfig {

	public static final Codec<PatPatClientProximityPacketsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("proximityPacketsEnabled", false, Codec.BOOL, PatPatClientProximityPacketsConfig::isProximityPacketsEnabled),
			option("maxPacketsPerSecond", 5, Codec.INT, PatPatClientProximityPacketsConfig::getMaxPacketsPerSecond),
			option("isBlacklist", false, Codec.BOOL, PatPatClientProximityPacketsConfig::isBlacklist)
	).apply(instance, PatPatClientProximityPacketsConfig::new));

	@Setter
	private boolean proximityPacketsEnabled;
	private int maxPacketsPerSecond;
	private boolean isBlacklist;

	public static Supplier<PatPatClientProximityPacketsConfig> getNewInstance() {
		return () -> CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public PatPatClientProximityPacketsConfig copy() {
		return new PatPatClientProximityPacketsConfig(
				this.proximityPacketsEnabled,
				this.maxPacketsPerSecond,
				this.isBlacklist
		);
	}

}
