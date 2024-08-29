package net.lopymine.patpat.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvent;

import com.mojang.serialization.Codec;

import java.util.UUID;

public class VersionedThings {

	public static final Codec<UUID> UUID_CODEC =
			/*? >=1.19.3 {*/net.minecraft.util.Uuids
			/*?} else {*//*net.minecraft.util.dynamic.DynamicSerializableUuid*//*?}*/
			.CODEC;

	public static final
			/*? >=1.19.3 {*/ net.minecraft.registry.Registry<EntityType<?>>
			/*?} else {*//*net.minecraft.util.registry.Registry*//*?}*/
			ENTITY_TYPE =
			/*? >=1.19.3 {*/ net.minecraft.registry.Registries
			/*?} else {*//*net.minecraft.util.registry.Registry*//*?}*/
			.ENTITY_TYPE;

	public static final
			/*? >=1.19.3 {*/ net.minecraft.registry.Registry<SoundEvent>
			/*?} else {*//*net.minecraft.util.registry.Registry*//*?}*/
			SOUND_EVENT =
			/*? >=1.19.3 {*/ net.minecraft.registry.Registries
			/*?} else {*//*net.minecraft.util.registry.Registry*//*?}*/
			.SOUND_EVENT;

	public static final String CLOTH_CONFIG_ID = /*? >=1.18 {*/"cloth-config"/*?} else {*//*"cloth-config2"*//*?}*/;

	private VersionedThings() {
		throw new IllegalStateException("Utility class");
	}
}
