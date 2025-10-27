package net.lopymine.patpat.utils;

import com.mojang.serialization.Codec;
import java.util.UUID;

import lombok.experimental.UtilityClass;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@UtilityClass
public class VersionedThings {

	public static final Codec<UUID> UUID_CODEC =
			/*? if >=1.19.3 {*/net.minecraft.core.UUIDUtil.AUTHLIB_CODEC;
			 /*?} elif >=1.19 {*//*net.minecraft.core.UUIDUtil.CODEC;
			  *//*?} else {*/ /*net.minecraft.core.SerializableUUID.CODEC;*//*?}*/

	public static final net.minecraft.core.Registry<EntityType<?>>
			ENTITY_TYPE =
			/*? >=1.19.3 {*/ net.minecraft.core.registries.BuiltInRegistries
			/*?} else {*//*net.minecraft.core.Registry*//*?}*/
			.ENTITY_TYPE;

	public static final net.minecraft.core.Registry<SoundEvent>
			SOUND_EVENT =
			/*? >=1.19.3 {*/ net.minecraft.core.registries.BuiltInRegistries
			/*?} else {*//*net.minecraft.core.Registry*//*?}*/
			.SOUND_EVENT;

	public static final String CLOTH_CONFIG_ID = /*? >=1.18 {*/"cloth-config"/*?} else {*//*"cloth-config2"*//*?}*/;

	public static Level getLevel(Entity entity) {
		/*? >=1.20 {*/
		return entity.level();
		/*?} else if >1.17.1 {*//*;
		return entity.getLevel();
		*//*?} else {*/
		/*return entity.level;
		*//*?}*/
	}

	public static AABB getAABBFromPosition(Vec3 vec3, double d, double e, double f) {
		/*? >1.16.5 {*/
		return AABB.ofSize(vec3, d, e, f);
		/*?} else {*/
		/*return new AABB(vec3.x - d / (double)2.0F, vec3.y - e / (double)2.0F, vec3.z - f / (double)2.0F, vec3.x + d / (double)2.0F, vec3.y + e / (double)2.0F, vec3.z + f / (double)2.0F);
		*//*?}*/
	}
}
