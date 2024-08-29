package net.lopymine.patpat.extension;

import net.minecraft.entity.*;
import net.minecraft.util.Identifier;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.VersionedThings;

public class EntityExtension {

	private EntityExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static String getTypeId(Entity entity) {
		return getId(entity.getType());
	}

	public static String getId(EntityType<?> entityType) {
		Identifier id = VersionedThings.ENTITY_TYPE.getId(entityType);
		if (id == null) {
			PatPatClient.warn("Failed to find entity type {}", entityType.getName());
			return "null";
		}
		return id.toString();
	}
}
