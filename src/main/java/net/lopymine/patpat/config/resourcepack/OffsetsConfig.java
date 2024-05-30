package net.lopymine.patpat.config.resourcepack;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@Getter
@Setter
public class OffsetsConfig {
	public static final OffsetsConfig EMPTY = new OffsetsConfig(0.0D, 0.0D, 0.0D);
	public static final Codec<OffsetsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.optionalFieldOf("offsetX", EMPTY.getOffsetX()).forGetter(OffsetsConfig::getOffsetX),
			Codec.DOUBLE.optionalFieldOf("offsetY", EMPTY.getOffsetY()).forGetter(OffsetsConfig::getOffsetY),
			Codec.DOUBLE.optionalFieldOf("offsetZ", EMPTY.getOffsetZ()).forGetter(OffsetsConfig::getOffsetZ)
	).apply(instance, OffsetsConfig::new));

	private double offsetX;
	private double offsetY;
	private double offsetZ;

	public OffsetsConfig(double offsetX, double offsetY, double offsetZ) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}
}
