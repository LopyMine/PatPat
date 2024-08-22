package net.lopymine.patpat.extension;

import net.minecraft.entity.*;

import net.lopymine.patpat.utils.VersionedThings;

public class EntityExtension {

	private EntityExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static String getTypeId(Entity entity) {
		return getId(entity.getType());
	}

	public static String getId(EntityType<?> entityType) {
		return VersionedThings.ENTITY_TYPE.getId(entityType).toString();
	}
}
