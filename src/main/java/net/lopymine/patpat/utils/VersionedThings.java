package net.lopymine.patpat.utils;

import com.mojang.serialization.Codec;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

@UtilityClass
public class VersionedThings {

	public static final Codec<UUID> UUID_CODEC =
			net.minecraft.core.UUIDUtil.AUTHLIB_CODEC;

	public static final net.minecraft.core.Registry<EntityType<?>>
			ENTITY_TYPE =
			net.minecraft.core.registries.BuiltInRegistries
					.ENTITY_TYPE;

	public static final net.minecraft.core.Registry<SoundEvent>
			SOUND_EVENT =
			net.minecraft.core.registries.BuiltInRegistries
					.SOUND_EVENT;

	public static final String CLOTH_CONFIG_ID = "cloth-config";

	public static Level getLevel(Entity entity) {
		return entity.level();
	}

	public static AABB getAABBFromPosition(Vec3 vec3, double d, double e, double f) {
		return AABB.ofSize(vec3, d, e, f);
	}
}
