package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.utils.*;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientMainConfig {

	public static final Codec<PatPatClientMainConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					option("modEnabled", true, Codec.BOOL, PatPatClientMainConfig::isModEnabled),
					option("debugLogEnabled", false, Codec.BOOL, PatPatClientMainConfig::isDebugLogEnabled),
					option("patCombination", PatPatKeybinding.DEFAULT_COMBINATION, KeybindingCombination.CODEC, PatPatClientMainConfig::getPatCombination))
			.apply(instance, PatPatClientMainConfig::new));

	private boolean modEnabled;
	private boolean debugLogEnabled;
	private KeybindingCombination patCombination;

	public static PatPatClientMainConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public void setDebugLogEnabled(boolean debugLogEnabled) {
		this.debugLogEnabled = debugLogEnabled;
		PatPatClient.LOGGER.setDebugMode(debugLogEnabled);
	}
}
