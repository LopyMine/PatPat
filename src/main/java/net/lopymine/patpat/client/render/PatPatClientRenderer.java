package net.lopymine.patpat.client.render;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.client.config.sub.PatPatClientVisualConfig;
import net.lopymine.patpat.client.render.feature.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.config.PatPatClientStatsConfig;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.compat.flashback.FlashbackCompat;
import net.lopymine.patpat.compat.replaymod.ReplayModCompat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.extension.VertexConsumerExtension;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.Nullable;

//? if <=1.21.4 {
/*import com.mojang.blaze3d.systems.RenderSystem;
 *//*?}*/

//? if >1.21.8 {
import org.joml.Quaternionf;
/*?}*/


@ExtensionMethod(VertexConsumerExtension.class)
public class PatPatClientRenderer {

	private static final Queue<PacketPat> serverPats = new ConcurrentLinkedQueue<>();
	private static final Queue<PacketPat> clientPats = new ConcurrentLinkedQueue<>();

	public static void registerServerPacket(PacketPat packet) {
		serverPats.offer(packet);
	}

	public static void registerClientPacket(PacketPat packet) {
		clientPats.offer(packet);
	}

	public record PacketPat(LivingEntity pattedEntity, PlayerConfig playerConfig, LocalPlayer player,
	                        boolean replayModPacket) {

		@Override
		public String toString() {
			return "PacketPat{" +
					"pattedEntity=" + this.pattedEntity.toString() +
					", playerConfig=" + this.playerConfig.toString() +
					", player=" + this.player.toString() +
					", replayModPacket=" + this.replayModPacket +
					'}';
		}
	}

	public static void register() {
		//? if <=1.21.8 {
		/*WorldRenderEvents.AFTER_ENTITIES.register(PatPatClientRenderer::renderPatOnYourself);
		*///?}
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			boolean frozen = /*? if >1.20.2 {*/ client.tickRateManager().isFrozen(); /*?} else {*/ /*false; *//*?}*/
			PatPatClientConfig config = PatPatClientConfig.getInstance();

			PacketPat packet;
			if (!frozen) {
				while ((packet = serverPats.poll()) != null) {
					LivingEntity pattedEntity = packet.pattedEntity();
					PlayerConfig playerConfig = packet.playerConfig();
					PatEntity patEntity = PatPatClientManager.pat(pattedEntity, playerConfig);

					if (config.getSoundsConfig().isSoundsEnabled() && !packet.replayModPacket()) {
						PatPatClientSoundManager.playSound(patEntity, packet.player(), config.getSoundsConfig().getSoundsVolume());
					}
				}
			}

			while ((packet = clientPats.poll()) != null) {
				LocalPlayer player = packet.player();
				LivingEntity pattedEntity = packet.pattedEntity();
				PlayerConfig playerConfig = packet.playerConfig();

				PatPatClientNetworkManager.sendPacketToServer(PatPatClientPacketManager.getPatPacket(pattedEntity));
				PatEntity patEntity = PatPatClientManager.pat(pattedEntity, playerConfig);

				PatPatClientStatsConfig statsConfig = PatPatClientStatsConfig.getInstance();
				statsConfig.count(pattedEntity);

				PatPatClientVisualConfig visualConfig = config.getVisualConfig();
				if (visualConfig.isClientSwingHandEnabled()) {
					player.swing(InteractionHand.MAIN_HAND, false);
				}
				if (visualConfig.isServerSwingHandEnabled() && !player.isSpectator()) {
					player.connection.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
				}

				ReplayModCompat.onPat(pattedEntity.getId(), player.getId());
				FlashbackCompat.onPat(pattedEntity.getId(), player.getId());
				PatPatClientProxLibPacketManager.onPat(pattedEntity.getId());

				if (config.getSoundsConfig().isSoundsEnabled()) {
					PatPatClientSoundManager.playSound(patEntity, player, config.getSoundsConfig().getSoundsVolume());
				}
			}

			if (!frozen) {
				PatPatClientManager.tickEntities();
			}
		});
	}

	public static void renderPatOnYourself(/*? if <1.21.9 {*//*WorldRenderContext context*//*?}*/) {
		if (!PatPatClientConfig.getInstance().getVisualConfig().isCameraShackingEnabled()) {
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		/*? if <=1.21.8 {*/
		/*MultiBufferSource consumers = context.consumers();
		PoseStack matrices = context.matrixStack();
		if (matrices == null || consumers == null) {
			return;
		}
		Camera camera = context.camera();
		*//*?} else {*/
		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
		/*?}*/
		if (player == null || camera.isDetached()) {
			return;
		}

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

		/*? if <1.21 {*/
		/*float tickDelta = context.tickDelta();
		*//*?} elif <1.21.9 {*/
		/*float tickDelta = context.tickCounter().getGameTimeDeltaPartialTick(false);
		*//*?} else {*/
		float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		/*?}*/
		int light = dispatcher.getPackedLightCoords(player, tickDelta);

		PatEntity patEntity = PatPatClientManager.getPatEntity(player);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity, tickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		PatPatClientRenderer.render(/*? if <=1.21.8 {*//*matrices, consumers, dispatcher*//*?} else {*/new PoseStack(), camera.rotation()/*?}*/, patEntity, player, new Vec3f(0.0F, Mth.lerp(tickDelta, camera.eyeHeightOld, camera.eyeHeight) - 0.2F, 0.0F), tickDelta, light);
	}

	public static RenderResult render(PoseStack matrices, /*? if <=1.21.8 {*//*MultiBufferSource provider, EntityRenderDispatcher dispatcher*//*?} else {*/Quaternionf cameraRotation/*?}*/, @Nullable PatEntity providedPatEntity, @Nullable Entity entity, @Nullable Vec3f overrideOffset, float tickDelta, int light) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return RenderResult.FAILED;
		}

		PatEntity patEntity = providedPatEntity;
		if(patEntity == null && entity instanceof LivingEntity livingEntity){
			patEntity = PatPatClientManager.getPatEntity(livingEntity);
		}

		if (patEntity == null) {
			return RenderResult.FAILED;
		}

		int numberToMirrorTexture = /*? if >=1.21 {*/1/*?} else {*/ /*-1*//*?}*/;

		CustomAnimationSettingsConfig animation = patEntity.getAnimation();
		FrameConfig frameConfig = animation.getFrameConfig();
		enableBlend();
		//? if <=1.20.4 {
		/*float nameLabelHeight = entity != null ? entity.getBbHeight() : 0.0F;
		 *///?} else {
		net.minecraft.world.phys.Vec3 vec3d = entity != null ? entity.getAttachments().getNullable(net.minecraft.world.entity.EntityAttachment.NAME_TAG, 0, entity.getViewYRot(tickDelta)) : null;
		float nameLabelHeight = vec3d != null ? (float) vec3d.y : 0.0F;
		//?}

		matrices.pushPose();
		matrices.translate(
				0.0F,
				overrideOffset != null ? overrideOffset.getY() : (nameLabelHeight * PatPatClientManager.getAnimationProgress(patEntity, tickDelta)) + 0.11F - frameConfig.offsetY() - config.getVisualConfig().getAnimationOffsets().getY(),
				0.0F
		);
		matrices.mulPose(/*? if <=1.21.8 {*//*dispatcher.cameraOrientation()*//*?} else {*/cameraRotation/*?}*/);
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

		/*? if <=1.21.8 {*/
		/*Pose peek = matrices.last();
		/^? if >=1.19.3 {^/org.joml.Matrix4f/^?} else {^/ /^com.mojang.math.Matrix4f^//^?}^/ matrix4f = peek.pose();
		VertexConsumer buffer = provider.getBuffer(RenderType.entityTranslucent(animation.getTexture()));

		buffer.withVertex(matrix4f, x1, y1, z).withColor(255, 255, 255, 255).withUv(u1, v1).withOverlay(OverlayTexture.NO_OVERLAY).withLight(light).withNormal(0, 1, 0).end();
		buffer.withVertex(matrix4f, x1, y2, z).withColor(255, 255, 255, 255).withUv(u1, v2).withOverlay(OverlayTexture.NO_OVERLAY).withLight(light).withNormal(0, 1, 0).end();
		buffer.withVertex(matrix4f, x2, y2, z).withColor(255, 255, 255, 255).withUv(u2, v2).withOverlay(OverlayTexture.NO_OVERLAY).withLight(light).withNormal(0, 1, 0).end();
		buffer.withVertex(matrix4f, x2, y1, z).withColor(255, 255, 255, 255).withUv(u2, v1).withOverlay(OverlayTexture.NO_OVERLAY).withLight(light).withNormal(0, 1, 0).end();
		*//*?} else {*/
		PatFeatureRenderer.getInstance().request(animation.getTexture(), matrices.last(), x1, y1, x2, y2, z, u1, v1, u2, v2, light);
		/*?}*/
		matrices.popPose();
		disableBlend();
		if (config.getVisualConfig().isHidingNicknameEnabled()) {
			return RenderResult.RENDERER_SHOULD_CANCEL;
		}
		return RenderResult.RENDERED;
	}

	public static void scaleEntityIfPatted(LivingEntity livingEntity, PoseStack matrixStack, float tickDelta) {
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
		//? if <=1.21.4 {
		/*RenderSystem.enableBlend();
		*//*?}*/
	}

	private static void disableBlend() {
		//? if <=1.21.4 {
		/*RenderSystem.disableBlend();
		*//*?}*/
	}

}
