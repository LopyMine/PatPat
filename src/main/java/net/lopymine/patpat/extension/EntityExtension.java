package net.lopymine.patpat.extension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.VersionedThings;
import net.lopymine.patpat.utils.mixin.MarkedEntity;

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
		return entity.getId();
	}

	public static void mark(Entity entity, boolean marked) {
		if (entity instanceof MarkedEntity markedEntity) {
			markedEntity.patPat$mark(marked);
		}
	}

	public static boolean isMarked(Entity entity) {
		if (entity instanceof MarkedEntity markedEntity) {
			return markedEntity.patPat$isMarked();
		}
		return false;
	}
}
