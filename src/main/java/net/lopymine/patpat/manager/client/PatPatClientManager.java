package net.lopymine.patpat.manager.client;

import net.minecraft.entity.LivingEntity;

import net.lopymine.patpat.config.resourcepack.PlayerConfig;
import net.lopymine.patpat.entity.PatEntity;

import java.util.*;
import org.jetbrains.annotations.*;

public class PatPatClientManager {

	private static final Map<UUID, PatEntity> PAT_ENTITIES = new HashMap<>();

	private PatPatClientManager() {
		throw new IllegalStateException("Manager class");
	}

	@Nullable
	public static PatEntity getPatEntity(@NotNull LivingEntity entity) {
		return PAT_ENTITIES.get(entity.getUuid());
	}

	public static void removePatEntity(@NotNull LivingEntity entity) {
		PAT_ENTITIES.remove(entity.getUuid());
	}

	public static void removePatEntity(@NotNull PatEntity patEntity) {
		LivingEntity entity = patEntity.getEntity();
		if (entity != null) {
			PatPatClientManager.removePatEntity(entity);
		}
	}

	public static PatEntity pat(@NotNull LivingEntity entity, @NotNull PlayerConfig whoPatted) {
		UUID uuid = entity.getUuid();
		PatEntity patEntity = PAT_ENTITIES.get(uuid);
		if (patEntity == null) {
			patEntity = new PatEntity(entity, whoPatted);
			PAT_ENTITIES.put(uuid, patEntity);
		} else {
			patEntity.resetAnimation();
		}
		return patEntity;
	}

	public static void reloadPatEntities() {
		PAT_ENTITIES.clear();
	}
}
