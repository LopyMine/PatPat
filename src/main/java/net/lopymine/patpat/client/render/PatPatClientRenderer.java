package net.lopymine.patpat.client.render;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
//? <=1.21.4 {
import com.mojang.blaze3d.systems.RenderSystem;
/*?} else {*/
/*import com.mojang.blaze3d.opengl.GlStateManager;
 *//*?}*/
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.compat.flashback.FlashbackCompat;
import net.lopymine.patpat.compat.replaymod.ReplayModCompat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.packet.PatPacket;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
public class PatPatClientRenderer {

	public static final List<PacketPat> serverPats = new ArrayList<>();
	public static final List<PacketPat> clientPats = new ArrayList<>();

	public record PacketPat(LivingEntity pattedEntity, PlayerConfig playerConfig, LocalPlayer player, boolean replayModPacket){}

	public static void register() {
		WorldRenderEvents.AFTER_ENTITIES.register(PatPatClientRenderer::renderPatOnYourself);
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			//? >1.20.2 {
			if (client.tickRateManager().isFrozen()) {
				return;
			}
			//?}
			PatPatClientConfig config = PatPatClientConfig.getInstance();

			serverPats.forEach((packet) -> {
				LivingEntity pattedEntity = packet.pattedEntity();
				PlayerConfig playerConfig = packet.playerConfig();
				PatEntity patEntity = PatPatClientManager.pat(pattedEntity, playerConfig);

				if (config.getSoundsConfig().isSoundsEnabled() && !packet.replayModPacket()) {
					PatPatClientSoundManager.playSound(patEntity, packet.player(), config.getSoundsConfig().getSoundsVolume());
				}
			});
			serverPats.clear();

			clientPats.forEach((patPacket) -> {
				LocalPlayer player = patPacket.player();
				LivingEntity pattedEntity = patPacket.pattedEntity();
				PlayerConfig playerConfig = patPacket.playerConfig();

				PatPatClientNetworkManager.sendPacketToServer(PatPatClientPacketManager.getPatPacket(pattedEntity));
				PatEntity patEntity = PatPatClientManager.pat(pattedEntity, playerConfig);

				if (config.getVisualConfig().isSwingHandEnabled()) {
					player.swing(InteractionHand.MAIN_HAND);
				}

				System.out.println("ENTITY WAS PATTED           DDDDDDDDDD");

				ReplayModCompat.onPat(pattedEntity.getId(), player.getId());
				FlashbackCompat.onPat(pattedEntity.getId(), player.getId());
				PatPatClientProxLibPacketManager.onPat(pattedEntity.getId());

				if (config.getSoundsConfig().isSoundsEnabled()) {
					PatPatClientSoundManager.playSound(patEntity, player, config.getSoundsConfig().getSoundsVolume());
				}
			});
			clientPats.clear();
			PatPatClientManager.tickEntities();
		});
	}

	private static void renderPatOnYourself(WorldRenderContext context) {
		if (!PatPatClientConfig.getInstance().getVisualConfig().isCameraShackingEnabled()) {
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		MultiBufferSource consumers = context.consumers();
		PoseStack matrices = context.matrixStack();
		Camera camera = context.camera();

		if (player == null || matrices == null || consumers == null || camera.isDetached()) {
			return;
		}

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		float tickDelta = context./*? if >=1.21 {*/ tickCounter().getGameTimeDeltaPartialTick(false); /*?} else {*/ /*tickDelta(); *//*?}*/
		int light = dispatcher.getPackedLightCoords(player, tickDelta);

		PatEntity patEntity = PatPatClientManager.getPatEntity(player);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity, tickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		PatPatClientRenderer.render(matrices, consumers, dispatcher, patEntity, player, new Vec3f(0.0F, Mth.lerp(tickDelta, camera.eyeHeightOld, camera.eyeHeight) - 0.2F, 0.0F), tickDelta, light);
	}

	public static RenderResult render(PoseStack matrices, MultiBufferSource provider, EntityRenderDispatcher dispatcher, @Nullable PatEntity providedPatEntity, @Nullable Entity entity, @Nullable Vec3f overrideOffset, float tickDelta, int light) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return RenderResult.FAILED;
		}

		PatEntity patEntity = providedPatEntity == null
				?
				entity instanceof LivingEntity livingEntity
						?
						PatPatClientManager.getPatEntity(livingEntity)
						:
						null
				:
				providedPatEntity;

		if (patEntity == null) {
			return RenderResult.FAILED;
		}

		int numberToMirrorTexture = /*? >=1.21 {*/1/*?} else {*/ /*-1*//*?}*/;

		CustomAnimationSettingsConfig animation = patEntity.getAnimation();
		FrameConfig frameConfig = animation.getFrameConfig();
		enableBlend();

		//? <=1.20.4 {
		/*float nameLabelHeight = entity.getHeight();
		 *///?} else {
		Vec3 vec3d = entity != null ? entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(tickDelta)) : null;
		float nameLabelHeight = vec3d != null ? (float) vec3d.y: 0.0F;
		//?}

		matrices.pushPose();
		matrices.translate(
				0.0F,
				overrideOffset != null ? overrideOffset.getY() : (nameLabelHeight * PatPatClientManager.getAnimationProgress(patEntity, tickDelta)) + 0.11F - frameConfig.offsetY() - config.getVisualConfig().getAnimationOffsets().getY(),
				0.0F
		);
		matrices.mulPose(dispatcher.cameraOrientation());
		matrices.scale(0.85F * numberToMirrorTexture, -0.85F, 0.85F);

		int frameWidth = animation.getTextureWidth() / frameConfig.totalFrames();
		int frameHeight = animation.getTextureHeight();

		float scaleX = 1;
		float scaleY = 1;
		if (frameHeight > frameWidth) {
			scaleX = (float) frameWidth / frameHeight;
		} else if (frameHeight < frameWidth) {
			scaleY = (float) frameHeight / frameWidth;
		}

		scaleX *= frameConfig.scaleX();
		scaleY *= frameConfig.scaleY();

		float x1 = -(scaleX / 2F) + (overrideOffset != null ? overrideOffset.getX() : (frameConfig.offsetX() + config.getVisualConfig().getAnimationOffsets().getX()));
		float x2 = x1 + scaleX;
		float y1 = -(scaleY / 2F);
		float y2 = y1 + scaleY;
		float z = -(overrideOffset != null ? overrideOffset.getZ() : (frameConfig.offsetZ() + config.getVisualConfig().getAnimationOffsets().getZ()));

		float framePercent = (float) 1 / frameConfig.totalFrames();
		float u1 = patEntity.getCurrentFrame() * framePercent;
		float u2 = u1 + framePercent;
		float v1 = 0.0F;
		float v2 = 1.0F;

		Pose peek = matrices.last();
		/*? >=1.19.3 {*/org.joml.Matrix4f/*?} else {*/ /*net.minecraft.util.math.Matrix4f*//*?}*/ matrix4f = peek./*? <=1.17.1 {*//*getModel()*//*?} else {*/pose()/*?}*/;
		VertexConsumer buffer = provider.getBuffer(RenderType.entityTranslucent(animation.getTexture()));

		buffer.addVertex(matrix4f, x1, y1, z).setColor(255, 255, 255, 255).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/
		buffer.addVertex(matrix4f, x1, y2, z).setColor(255, 255, 255, 255).setUv(u1, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/
		buffer.addVertex(matrix4f, x2, y2, z).setColor(255, 255, 255, 255).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/
		buffer.addVertex(matrix4f, x2, y1, z).setColor(255, 255, 255, 255).setUv(u2, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/

		matrices.popPose();
		disableBlend();
		if (config.getVisualConfig().isHidingNicknameEnabled()) {
			return RenderResult.RENDERER_SHOULD_CANCEL;
		}
		return RenderResult.RENDERED;
	}

	public static void scaleEntityIfPatted(LivingEntity livingEntity, PoseStack matrixStack, float tickDelta){
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity, tickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		matrixStack.scale(1F, PatPatClientManager.getAnimationProgress(patEntity, tickDelta), 1F);
	}

	public enum RenderResult {
		RENDERED,
		RENDERER_SHOULD_CANCEL,
		FAILED
	}

	private static void enableBlend() {
		//? <=1.21.4 {
		RenderSystem.enableBlend();
		/*?} else {*/
		/*GlStateManager._enableBlend();
		 *//*?}*/
	}

	private static void disableBlend() {
		//? <=1.21.4 {
		RenderSystem.disableBlend();
		/*?} else {*/
		/*GlStateManager._disableBlend();
		 *//*?}*/
	}

}
