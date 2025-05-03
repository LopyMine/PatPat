package net.lopymine.patpat.common.config.vector;

import lombok.*;
import net.minecraft.util.Util;

import com.mojang.serialization.Codec;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class Vec3f {

	private float x;
	private float y;
	private float z;

	public static final Codec<Vec3f> CODEC = Codec.FLOAT.listOf()
			.xmap(
					(coordinates) -> new Vec3f(coordinates.get(0), coordinates.get(1), coordinates.get(2)),
					(vec) -> List.of(vec.getX(), vec.getY(), vec.getZ())
			);

	public Vec3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vec3f vec3f)) return false;
		return Float.compare(x, vec3f.x) == 0 && Float.compare(y, vec3f.y) == 0 && Float.compare(z, vec3f.z) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
