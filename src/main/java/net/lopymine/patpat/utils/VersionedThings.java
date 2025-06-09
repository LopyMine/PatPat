package net.lopymine.patpat.utils;

import com.mojang.serialization.Codec;

import java.util.UUID;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;

public class VersionedThings {

	public static final Codec<UUID> UUID_CODEC =
			/*? >=1.19.3 {*/net.minecraft.core.UUIDUtil
			/*?} else {*//*net.minecraft.util.dynamic.DynamicSerializableUuid*//*?}*/
			.AUTHLIB_CODEC;

	public static final
			/*? >=1.19.3 {*/ net.minecraft.core.Registry<EntityType<?>>
			/*?} else {*//*net.minecraft.util.registry.Registry<EntityType<?>>*//*?}*/
			ENTITY_TYPE =
			/*? >=1.19.3 {*/ net.minecraft.core.registries.BuiltInRegistries
			/*?} else {*//*net.minecraft.util.registry.Registry*//*?}*/
			.ENTITY_TYPE;

	public static final
			/*? >=1.19.3 {*/ net.minecraft.core.Registry<SoundEvent>
			/*?} else {*//*net.minecraft.util.registry.Registry<SoundEvent>*//*?}*/
			SOUND_EVENT =
			/*? >=1.19.3 {*/ net.minecraft.core.registries.BuiltInRegistries
			/*?} else {*//*net.minecraft.util.registry.Registry*//*?}*/
			.SOUND_EVENT;

	public static final String CLOTH_CONFIG_ID = /*? >=1.18 {*/"cloth-config"/*?} else {*//*"cloth-config2"*//*?}*/;

	private VersionedThings() {
		throw new IllegalStateException("Utility class");
	}
}
