package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.utils.CodecUtils;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientVisualConfig {

	public static final PatPatClientVisualConfig DEFAULT = new PatPatClientVisualConfig(
			true,
			true,
			true,
			new Vec3f(),
			true,
			0.425F
	);

	public static final Codec<PatPatClientVisualConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("hidingNicknameEnabled", DEFAULT.hidingNicknameEnabled, Codec.BOOL, PatPatClientVisualConfig::isHidingNicknameEnabled),
			option("clientSwingHandEnabled", DEFAULT.clientSwingHandEnabled, Codec.BOOL, PatPatClientVisualConfig::isClientSwingHandEnabled),
			option("serverSwingHandEnabled", DEFAULT.serverSwingHandEnabled, Codec.BOOL, PatPatClientVisualConfig::isServerSwingHandEnabled),
			option("animationOffsets", DEFAULT.animationOffsets, Vec3f.CODEC, PatPatClientVisualConfig::getAnimationOffsets),
			option("cameraShackingEnabled", DEFAULT.cameraShackingEnabled, Codec.BOOL, PatPatClientVisualConfig::isCameraShackingEnabled),
			option("patWeight", DEFAULT.patWeight, Codec.FLOAT, PatPatClientVisualConfig::getPatWeight)
	).apply(instance, PatPatClientVisualConfig::new));

	private boolean hidingNicknameEnabled;
	private boolean clientSwingHandEnabled;
	private boolean serverSwingHandEnabled;
	private Vec3f animationOffsets;
	private boolean cameraShackingEnabled;
	private float patWeight;

	public static PatPatClientVisualConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	public PatPatClientVisualConfig copy() {
		return new PatPatClientVisualConfig(
				this.hidingNicknameEnabled,
				this.clientSwingHandEnabled,
				this.serverSwingHandEnabled,
				this.animationOffsets,
				this.cameraShackingEnabled,
				this.patWeight
		);
	}

}
