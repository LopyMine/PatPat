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

	public static final Codec<PatPatClientProximityPacketsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("proximityPacketsEnabled", true, Codec.BOOL, PatPatClientProximityPacketsConfig::isProximityPacketsEnabled),
			option("maxPacketsPerSecond", 10, Codec.INT, PatPatClientProximityPacketsConfig::getMaxPacketsPerSecond)
	).apply(instance, PatPatClientProximityPacketsConfig::new));

	private boolean proximityPacketsEnabled;
	private int maxPacketsPerSecond;

	private PatPatClientProximityPacketsConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientProximityPacketsConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public void setProximityPacketsEnabled(boolean value){
		this.proximityPacketsEnabled = value;
		PatPatClientProxLibManager.setEnabled(value);
	}

}
