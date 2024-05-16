package net.lopymine.patpat.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FrameConfig(float width, float height, float offsetX, float offsetY) {

	public static final FrameConfig DEFAULT_FRAME = new FrameConfig(1, 1, 0, 0);

	// TODO
	//  Использовать переменные в рендере (Придумать как правильно использовать width и height, их стандартные значения)

	public static final Codec<FrameConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("width").forGetter(FrameConfig::width),
		Codec.FLOAT.fieldOf("height").forGetter(FrameConfig::height),
		Codec.FLOAT.fieldOf("offsetX").orElse(0F).forGetter(FrameConfig::offsetX),
		Codec.FLOAT.fieldOf("offsetY").orElse(0F).forGetter(FrameConfig::offsetY)
	).apply(instance, FrameConfig::new));

}