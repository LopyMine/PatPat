package net.lopymine.patpat.config.resourcepack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FrameConfig(int totalFrames, int frameWidth, int frameHeight, OffsetsConfig offsetsConfig) {
	public static final FrameConfig DEFAULT_FRAME = new FrameConfig(5, 112, 112, OffsetsConfig.EMPTY);

	public static final Codec<FrameConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("totalFrames").orElse(DEFAULT_FRAME.totalFrames()).forGetter(FrameConfig::totalFrames),
			Codec.INT.fieldOf("frameWidth").orElse(DEFAULT_FRAME.frameWidth()).forGetter(FrameConfig::frameWidth),
			Codec.INT.fieldOf("frameHeight").orElse(DEFAULT_FRAME.frameHeight()).forGetter(FrameConfig::frameHeight),
			OffsetsConfig.CODEC.optionalFieldOf("handOffsets", DEFAULT_FRAME.offsetsConfig()).forGetter(FrameConfig::offsetsConfig)
	).apply(instance, FrameConfig::new));
}