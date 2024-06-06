package net.lopymine.patpat.config.resourcepack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FrameConfig(int totalFrames, int frameWidth, int frameHeight, float animationOffsetX, float animationOffsetY, float animationOffsetZ) {
	public static final FrameConfig DEFAULT_FRAME = new FrameConfig(5, 112, 112, 0F, 0F, 0F);

	public static final Codec<FrameConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("totalFrames").orElse(DEFAULT_FRAME.totalFrames()).forGetter(FrameConfig::totalFrames),
			Codec.INT.fieldOf("width").orElse(DEFAULT_FRAME.frameWidth()).forGetter(FrameConfig::frameWidth),
			Codec.INT.fieldOf("weight").orElse(DEFAULT_FRAME.frameHeight()).forGetter(FrameConfig::frameHeight),
			Codec.FLOAT.optionalFieldOf("offsetX", DEFAULT_FRAME.animationOffsetX()).forGetter(FrameConfig::animationOffsetX),
			Codec.FLOAT.optionalFieldOf("offsetY", DEFAULT_FRAME.animationOffsetY()).forGetter(FrameConfig::animationOffsetY),
			Codec.FLOAT.optionalFieldOf("offsetZ", DEFAULT_FRAME.animationOffsetZ()).forGetter(FrameConfig::animationOffsetZ)
	).apply(instance, FrameConfig::new));
}