package net.lopymine.patpat.client.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import net.fabricmc.loader.api.FabricLoader;
import net.lopymine.patpat.extension.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;

import com.mojang.authlib.GameProfile;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.list.PatPatClientIgnoreMobListConfig;
import net.lopymine.patpat.client.config.resourcepack.CustomAnimationSettingsConfig;
import net.lopymine.patpat.client.config.resourcepack.PlayerConfig;
import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.PatPacket;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.utils.ProfilerUtils;
import net.lopymine.patpat.utils.VersionedThings;

import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(value = {EntityExtension.class, GameProfileExtension.class})
public class PatPatClientManager {

	private static final Map<UUID, PatEntity> PAT_ENTITIES = new HashMap<>();

	public static final PatLogger LOGGER = PatPatClient.LOGGER.extend("PatManager");

	@Setter
	@Getter
	private static int patCooldown = 0;

	private PatPatClientManager() {
		throw new IllegalStateException("Manager class");
	}

	@Nullable
	public static PatEntity getPatEntity(@NotNull LivingEntity entity) {
		return PAT_ENTITIES.get(entity.getUUID());
	}

	public static void tickEntities() {
		PAT_ENTITIES.forEach((uuid, patEntity) -> patEntity.tick());
	}

	public static void removePatEntity(@NotNull LivingEntity entity) {
		PAT_ENTITIES.remove(entity.getUUID());
	}

	public static void removePatEntity(@NotNull PatEntity patEntity) {
		LivingEntity entity = patEntity.getEntity();
		if (entity != null) {
			PatPatClientManager.removePatEntity(entity);
		}
	}

	public static PatEntity pat(@NotNull LivingEntity entity, @NotNull PlayerConfig whoPatted) {
		LOGGER.debug("{} just patted {}", whoPatted.getName(), entity.getName().getString());

		UUID uuid = entity.getUUID();
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

		int frame = Mth.clamp((int) Math.floor(totalFrames * animationProgress), 0, totalFrames - 1);
		patEntity.setCurrentFrame(frame);

		float range = PatPatClientConfig.getInstance().getVisualConfig().getPatWeight() / patEntity.getEntity().getBbHeight();
		return ((float) ((1 - range) + range * (1 - Math.sin(animationProgress * Math.PI))));
	}

	public static void clearPatEntities() {
		PAT_ENTITIES.clear();
	}

	public static void requestPat() {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return;
		}

		if (!PatPatClientKeybindingManager.getPatKeybinding().isDown()) {
			return;
		}

		if (patCooldown != 0) {
			LOGGER.debug("Pat rejected: Cooldown");
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;

		if (player == null) {
			LOGGER.debug("Pat rejected: Player is null");
			return;
		}

		if (player.isDeadOrDying()) {
			LOGGER.debug("Pat rejected: Player is dead");
			return;
		}

		LivingEntity pattedEntity = PatPatClientManager.getPatEntityFromHitResult();
		if (pattedEntity == null) {
			LOGGER.debug("Pat rejected: Patted entity is null");
			return;
		}

		PatPatClientRenderer.registerClientPacket(new PatPacket(pattedEntity, PlayerConfig.currentSession(), player, false));

		PatPatClientManager.patCooldown = 4;
	}

	public static boolean canPat() {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return false;
		}
		if (!PatPatClientKeybindingManager.getPatKeybinding().isDown()) {
			return false;
		}
		return getPatEntityFromHitResult() != null;
	}

	@Nullable
	public static LivingEntity getPatEntityFromHitResult() {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null || minecraft.level == null || player.isDeadOrDying()) {
			return null;
		}

		if (!player.getMainHandItem().isEmpty()) {
			LOGGER.debug("Pat rejected: Main hand is not empty");
			return null;
		}

		Entity cameraEntity = minecraft.getCameraEntity();
		if (cameraEntity == null) {
			LOGGER.debug("Pat rejected: Camera entity is null");
			return null;
		}

		ProfilerUtils.push("patpat$pick");
		//? if >=1.20.5 {
		double blockInteractionRange = player.blockInteractionRange();
		double entityInteractionRange = player.entityInteractionRange();
		//?}

		//? if >=1.21.2 {
		float tickDelta = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
		//?} elif >=1.21 {
		/*float tickDelta = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
		 *///?} else {
		/*float tickDelta = minecraft.getFrameTime();
		 *///?}

		cameraEntity.mark(true);
		//? if >=1.20.5 {
		HitResult result = minecraft.gameRenderer.pick(cameraEntity, blockInteractionRange, entityInteractionRange, tickDelta);
		//?} else {
		/*HitResult oldResult = minecraft.hitResult;
		minecraft.gameRenderer.pick(tickDelta);
		HitResult result = minecraft.hitResult;
		minecraft.hitResult = oldResult;
		*///?}
		cameraEntity.mark(false);
		ProfilerUtils.pop();

		if (!(result instanceof EntityHitResult hitResult)) {
			LOGGER.debug("Pat rejected: No entity in crosshair");
			return null;
		}

		if (hasActiveLeash(player)) {
			LOGGER.debug("Pat rejected: Player has lead");
			return null;
		}

		if (!(hitResult.getEntity() instanceof LivingEntity pattedEntity)) {
			LOGGER.debug("Pat rejected: Entity is not LivingEntity");
			return null;
		}

		if (pattedEntity.isInvisible()) {
			LOGGER.debug("Pat rejected: Patted entity is invisible");
			return null;
		}

		if (PatPatClientIgnoreMobListConfig.getInstance().isIgnored(pattedEntity.getType())) {
			LOGGER.debug("Pat rejected: Entity type in IgnoreMobList");
			return null;
		}

		return pattedEntity;
	}


	private static boolean hasActiveLeash(Player player) {
		double d = 32.0F;
		AABB aABB = VersionedThings.getAABBFromPosition(player.getPosition(0), d, d, d);

		for (Entity entity : VersionedThings.getLevel(player).getEntitiesOfClass(Entity.class, aABB)) {
			//? >1.20.6 {
			if (!(entity instanceof Leashable leashable)) {
				continue;
			}
			if (leashable.getLeashHolder() == player) {
				return true;
			}
			//?} else {
			/*if (!(entity instanceof Mob mob)) {
				continue;
			}
			if (mob.getLeashHolder() == player) {
				return true;
			}
			*///?}
		}
		return false;
	}
}
