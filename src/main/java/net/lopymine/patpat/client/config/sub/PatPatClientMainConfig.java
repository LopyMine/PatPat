package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.InputType;
import net.lopymine.patpat.utils.*;

import java.util.*;
import java.util.stream.Collectors;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientMainConfig {

	public static final Codec<PatPatClientMainConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("modEnabled", true, Codec.BOOL, PatPatClientMainConfig::isModEnabled),
			option("debugLogEnabled", false, Codec.BOOL, PatPatClientMainConfig::isDebugLogEnabled),
			Codec.unboundedMap(Codec.INT, InputType.CODEC).optionalFieldOf("patKeys")
					.xmap((o) -> o.orElse(new HashMap<>()), Optional::of)
					.xmap((map) -> map.entrySet()
									.stream()
									.collect(Collectors.toMap(
											(e) -> e.getValue().toVanillaType().getOrCreate(e.getKey()),
											(e) -> e.getValue().toVanillaType()
									)),
							(map) -> map.entrySet()
									.stream()
									.collect(Collectors.toMap(
											(e) -> e.getKey().getValue(),
											(e) -> InputType.of(e.getValue())
									))
					).forGetter(PatPatClientMainConfig::getPatKeys))
		.apply(instance, PatPatClientMainConfig::new));

	private boolean modEnabled;
	private boolean debugLogEnabled;
	private Map<InputConstants.Key, InputConstants.Type> patKeys;

	private PatPatClientMainConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientMainConfig getNewInstance() {

		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public void setDebugLogEnabled(boolean debugLogEnabled) {
		this.debugLogEnabled = debugLogEnabled;
		PatPatClient.LOGGER.setDebugMode(debugLogEnabled);
	}
}
