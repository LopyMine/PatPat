package net.lopymine.patpat.client.config.sub;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.utils.*;

import static net.lopymine.patpat.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PatPatClientVisualConfig {

	public static final Codec<PatPatClientVisualConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("hidingNicknameEnabled", true, Codec.BOOL, PatPatClientVisualConfig::isHidingNicknameEnabled),
			option("swingHandEnabled", true, Codec.BOOL, PatPatClientVisualConfig::isSwingHandEnabled),
			option("animationOffsets", new Vec3f(), Vec3f.CODEC, PatPatClientVisualConfig::getAnimationOffsets),
			option("cameraShackingEnabled", true, Codec.BOOL, PatPatClientVisualConfig::isCameraShackingEnabled),
			option("patWeight", 0.425F, Codec.FLOAT, PatPatClientVisualConfig::getPatWeight)
	).apply(instance, PatPatClientVisualConfig::new));

	private boolean hidingNicknameEnabled;
	private boolean swingHandEnabled;
	private Vec3f animationOffsets;
	private boolean cameraShackingEnabled;
	private float patWeight;

	private PatPatClientVisualConfig() {
		throw new IllegalArgumentException();
	}

	public static PatPatClientVisualConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

}
