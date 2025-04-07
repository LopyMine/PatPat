package net.lopymine.patpat.manager.client;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.entity.PatEntity;

import java.util.*;
import org.jetbrains.annotations.*;

public class PatPatClientManager {

	private static final Map<UUID, PatEntity> PAT_ENTITIES = new HashMap<>();

	private PatPatClientManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			//? >1.20.2 {
			if (client.getTickManager().isFrozen()) {
				return;
			}
			//?}
			PatPatClientManager.tickEntities();
		});
	}

	@Nullable
	public static PatEntity getPatEntity(@NotNull LivingEntity entity) {
		return PAT_ENTITIES.get(entity.getUuid());
	}

	public static void tickEntities() {
		PAT_ENTITIES.forEach((uuid, patEntity) -> patEntity.tick());
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
		PatPatClient.LOGGER.debug("{} patted {}", whoPatted.getName(), entity.getName());
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

	public static boolean expired(PatEntity patEntity, float tickDelta) {
		CustomAnimationSettingsConfig animationConfig = patEntity.getAnimation();
		int duration = animationConfig.getDuration();
		return patEntity.getProgress(tickDelta) > duration;
	}

	public static float getAnimationProgress(PatEntity patEntity, float tickDelta) {
		CustomAnimationSettingsConfig animationConfig = patEntity.getAnimation();
		int duration = animationConfig.getDuration();

		float animationProgress = patEntity.getProgress(tickDelta) / duration;
		animationProgress = (float) (1 - Math.pow(1 - animationProgress, 2));
		int totalFrames = animationConfig.getFrameConfig().totalFrames();

		int frame = MathHelper.clamp((int) Math.floor(totalFrames * animationProgress), 0, totalFrames - 1);
		patEntity.setCurrentFrame(frame);

		float range = PatPatClient.getConfig().getPatWeight() / patEntity.getEntity().getHeight();
		return ((float) ((1 - range) + range * (1 - Math.sin(animationProgress * Math.PI))));
	}

	public static void reloadPatEntities() {
		PAT_ENTITIES.clear();
	}
}
