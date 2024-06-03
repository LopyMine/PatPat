package net.lopymine.patpat.config.resourcepack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FrameConfig(int totalFrames, int frameWidth, int frameHeight, double animationOffsetX, double animationOffsetY, double animationOffsetZ) {
	public static final FrameConfig DEFAULT_FRAME = new FrameConfig(5, 112, 112, 0D, 0D, 0D);

	public static final Codec<FrameConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("totalFrames").orElse(DEFAULT_FRAME.totalFrames()).forGetter(FrameConfig::totalFrames),
			Codec.INT.fieldOf("frameWidth").orElse(DEFAULT_FRAME.frameWidth()).forGetter(FrameConfig::frameWidth),
			Codec.INT.fieldOf("frameHeight").orElse(DEFAULT_FRAME.frameHeight()).forGetter(FrameConfig::frameHeight),
			Codec.DOUBLE.optionalFieldOf("animationOffsetX", DEFAULT_FRAME.animationOffsetX()).forGetter(FrameConfig::animationOffsetX),
			Codec.DOUBLE.optionalFieldOf("animationOffsetY", DEFAULT_FRAME.animationOffsetY()).forGetter(FrameConfig::animationOffsetY),
			Codec.DOUBLE.optionalFieldOf("animationOffsetZ", DEFAULT_FRAME.animationOffsetZ()).forGetter(FrameConfig::animationOffsetZ)
	).apply(instance, FrameConfig::new));
}