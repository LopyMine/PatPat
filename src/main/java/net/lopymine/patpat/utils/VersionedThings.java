package net.lopymine.patpat.utils;

import com.mojang.serialization.Codec;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;

public class VersionedThings {

	public static final Codec<UUID> UUID_CODEC = UUIDUtil/*? if >=1.19.3 {*/.AUTHLIB_CODEC; /*?} else {*/ /*.CODEC; *//*?}*/

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

	private VersionedThings() {
		throw new IllegalStateException("Utility class");
	}
}
