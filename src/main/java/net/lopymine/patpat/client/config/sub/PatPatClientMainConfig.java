package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.utils.CodecUtils;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientMainConfig {

	public static final PatPatClientMainConfig DEFAULT = new PatPatClientMainConfig(
			true,
			false,
			PatPatKeybinding.DEFAULT_COMBINATION
	);

	public static final Codec<PatPatClientMainConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					option("modEnabled", DEFAULT.modEnabled, Codec.BOOL, PatPatClientMainConfig::isModEnabled),
					option("debugLogEnabled", DEFAULT.debugLogEnabled, Codec.BOOL, PatPatClientMainConfig::isDebugLogEnabled),
					option("patCombination", DEFAULT.patCombination, KeybindingCombination.CODEC, PatPatClientMainConfig::getPatCombination))
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

	public PatPatClientMainConfig copy() {
		return new PatPatClientMainConfig(
				this.modEnabled,
				this.debugLogEnabled,
				this.patCombination
		);
	}
}
