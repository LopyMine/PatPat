package net.lopymine.patpat.common.config.vector;

import lombok.*;
import net.minecraft.util.Util;

import com.mojang.serialization.Codec;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Vec3f {

	private float x;
	private float y;
	private float z;

	public static final Codec<Vec3f> CODEC = Codec.FLOAT.listOf()
			.comapFlatMap(
					(coordinates) -> Util.decodeFixedLengthList(coordinates, 3)
							.map((list) -> new Vec3f(list.get(0), list.get(1), list.get(2))),
					(vec) -> List.of(vec.getX(), vec.getY(), vec.getZ())
			);

	public Vec3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
}
