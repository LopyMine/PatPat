package net.lopymine.patpat.client.config.sub;

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

	public static final PatPatClientProximityPacketsConfig DEFAULT = new PatPatClientProximityPacketsConfig(
			true,
			10
	);

	public static final Codec<PatPatClientProximityPacketsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("proximityPacketsEnabled", DEFAULT.proximityPacketsEnabled, Codec.BOOL, PatPatClientProximityPacketsConfig::isProximityPacketsEnabled),
			option("maxPacketsPerSecond", DEFAULT.maxPacketsPerSecond, Codec.INT, PatPatClientProximityPacketsConfig::getMaxPacketsPerSecond)
	).apply(instance, PatPatClientProximityPacketsConfig::new));

	private boolean proximityPacketsEnabled;
	private int maxPacketsPerSecond;

	public static PatPatClientProximityPacketsConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public void setProximityPacketsEnabled(boolean value) {
		this.proximityPacketsEnabled = value;
		PatPatClientProxLibManager.setEnabled(value);
	}

	public PatPatClientProximityPacketsConfig copy() {
		return new PatPatClientProximityPacketsConfig(
				this.proximityPacketsEnabled,
				this.maxPacketsPerSecond
		);
	}

}
