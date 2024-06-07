package net.lopymine.patpat.config.resourcepack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FrameConfig(int totalFrames, float scaleX, float scaleY, float offsetX, float offsetY, float offsetZ) {
	public static final FrameConfig DEFAULT_FRAME = new FrameConfig(5, 0.9F, 0.9F, 0F, 0F, 0F);

	public static final Codec<FrameConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("totalFrames").orElse(DEFAULT_FRAME.totalFrames()).forGetter(FrameConfig::totalFrames),
			Codec.FLOAT.optionalFieldOf("scaleX", DEFAULT_FRAME.scaleX()).forGetter(FrameConfig::scaleX),
			Codec.FLOAT.optionalFieldOf("scaleY", DEFAULT_FRAME.scaleY()).forGetter(FrameConfig::scaleY),
			Codec.FLOAT.optionalFieldOf("offsetX", DEFAULT_FRAME.offsetX()).forGetter(FrameConfig::offsetX),
			Codec.FLOAT.optionalFieldOf("offsetY", DEFAULT_FRAME.offsetY()).forGetter(FrameConfig::offsetY),
			Codec.FLOAT.optionalFieldOf("offsetZ", DEFAULT_FRAME.offsetZ()).forGetter(FrameConfig::offsetZ)
	).apply(instance, FrameConfig::new));
}