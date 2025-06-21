package net.lopymine.patpat.client.manager;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.PacketPat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.extension.EntityExtension;
import net.lopymine.patpat.utils.ProfilerUtils;

import lombok.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;

import com.mojang.authlib.GameProfile;

import java.util.*;
import org.jetbrains.annotations.*;

@ExtensionMethod(EntityExtension.class)
public class PatPatClientManager {

	private static final Map<UUID, PatEntity> PAT_ENTITIES = new HashMap<>();

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
		PatPatClient.LOGGER.debug("{} patted {}", whoPatted.getName(), entity.getName());

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

		if (patCooldown != 0 || !PatPatClientKeybindingManager.PAT_KEYBINDING.isDown()) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;

		if (minecraft.player == null || minecraft.player.isDeadOrDying()) {
			return;
		}

		LivingEntity pattedEntity = PatPatClientManager.getPatEntityFromHitResult();
		if (pattedEntity == null) {
			return;
		}

		GameProfile profile = /*? if >=1.20.2 {*/ minecraft.getGameProfile(); /*?} else {*/ /*minecraft.getUser().getGameProfile(); *//*?}*/

		PlayerConfig whoPatted = PlayerConfig.of(profile.getName(), profile.getId());
		PatPatClientRenderer.clientPats.add(new PacketPat(pattedEntity, whoPatted, player, false));

		PatPatClientManager.patCooldown = 4;
	}

	public static boolean canPat() {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return false;
		}
		if (!PatPatClientKeybindingManager.PAT_KEYBINDING.isDown()) {
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

		Entity cameraEntity = minecraft.getCameraEntity();
		if (cameraEntity == null) {
			return null;
		}

		ProfilerUtils.push("patpat$pick");
		//? if >=1.20.5 {
		/*double blockInteractionRange = player.blockInteractionRange();
		double entityInteractionRange = player.entityInteractionRange();
		*///?}
		
		//? if >=1.21.2 {
		/*float tickDelta = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
		*///?} elif >=1.21 {
		/*float tickDelta = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
		*///?} else {
		float tickDelta = minecraft.getFrameTime();
		//?}

		cameraEntity.mark(true);
		//? if >=1.20.5 {
		/*HitResult result = minecraft.gameRenderer.pick(cameraEntity, blockInteractionRange, entityInteractionRange, tickDelta);
		*///?} else {
		HitResult oldResult = minecraft.hitResult;
		minecraft.gameRenderer.pick(tickDelta);
		HitResult result = minecraft.hitResult;
		minecraft.hitResult = oldResult;
		//?}
		cameraEntity.mark(false);
		ProfilerUtils.pop();

		if (!(result instanceof EntityHitResult hitResult) || !(hitResult.getEntity() instanceof LivingEntity pattedEntity)) {
			return null;
		}

		if (pattedEntity.isInvisible()) {
			return null;
		}

		return pattedEntity;
	}
}
