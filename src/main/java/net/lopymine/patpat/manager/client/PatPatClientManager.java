package net.lopymine.patpat.manager.client;

import net.minecraft.entity.LivingEntity;

import net.lopymine.patpat.entity.PatEntity;

import java.util.*;
import org.jetbrains.annotations.*;

public class PatPatClientManager {
	private static final Map<UUID, PatEntity> PAT_ENTITIES = new HashMap<>();
	private static final Set<UUID> MOD_AUTHORS = Set.of(
			UUID.fromString("192e3748-12d5-4573-a8a5-479cd394a1dc"), // LopyMine
			UUID.fromString("7b829ed5-9b74-428f-9b4d-ede06975fbc1") // nikita51
	);

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

	public static PatEntity pat(@NotNull LivingEntity entity, @Nullable UUID pattingPlayerUuid) {
		UUID uuid = entity.getUuid();
		PatEntity patEntity = PAT_ENTITIES.get(uuid);
		if (patEntity == null) {
			patEntity = new PatEntity(entity, pattingPlayerUuid != null && MOD_AUTHORS.contains(pattingPlayerUuid));
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
