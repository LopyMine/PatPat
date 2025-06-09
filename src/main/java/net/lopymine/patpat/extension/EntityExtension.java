package net.lopymine.patpat.extension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.utils.VersionedThings;

public class EntityExtension {

	private EntityExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static String getTypeId(Entity entity) {
		return getId(entity.getType());
	}

	public static String getId(EntityType<? extends Entity> entityType) {
		ResourceLocation id = VersionedThings.ENTITY_TYPE.getKey(entityType);
		if (id == null) {
			PatPatClient.LOGGER.warn("Failed to find entity type {}", entityType.getDescription());
			return "null";
		}
		return id.toString();
	}

	public static int getEntityIntId(Entity entity) {
		return entity./*? if >=1.21 {*/ getId /*?} else {*/ /*getEntityId *//*?}*/();
	}
}
