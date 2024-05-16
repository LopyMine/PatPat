package net.lopymine.patpat.extension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;

public class EntityExtension {

	private EntityExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static String getTypeId(Entity entity) {
		return getId(entity.getType());
	}

	public static String getId(EntityType<?> entityType) {
		return Registries.ENTITY_TYPE.getId(entityType).toString();
	}
}
