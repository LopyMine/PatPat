package net.lopymine.patpat.client.render;

import lombok.experimental.ExtensionMethod;
import net.fabricmc.loader.api.FabricLoader;
import net.lopymine.patpat.client.config.sub.PatPatClientVisualConfig;
import net.lopymine.patpat.client.render.feature.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
//? if <=1.19.2 {
/*import com.mojang.math.Quaternion;
*///?} else {
import org.joml.Quaternionf;
//?}

//? if <=1.21.4 {
/*import com.mojang.blaze3d.systems.RenderSystem;
 *//*?}*/

//? if <=1.21.8 {
/*import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
*//*?}*/


@ExtensionMethod(VertexConsumerExtension.class)
public class PatPatClientRenderer {

	private static final Queue<PatPacket> serverPats = new ConcurrentLinkedQueue<>();
	private static final Queue<PatPacket> clientPats = new ConcurrentLinkedQueue<>();

	public static void registerServerPacket(PatPacket packet) {
		serverPats.offer(packet);
	}

	public static void registerClientPacket(PatPacket packet) {
		clientPats.offer(packet);
	}

	public record PatPacket(LivingEntity pattedEntity, PlayerConfig playerConfig, LocalPlayer player,
	                        boolean replayModPacket) {

		@Override
		public String toString() {
			return "PatPacket{" +
					"pattedEntity=" + this.pattedEntity.toString() +
					", playerConfig=" + this.playerConfig.toString() +
					", player=" + this.player.toString() +
					", replayModPacket=" + this.replayModPacket +
					'}';
		}
	}

	public static void register() {
		//? if <=1.21.8 {
		/*WorldRenderEvents.AFTER_ENTITIES.register((__) -> {
			PatPatClientRenderer.renderPatOnYourself();
			PatFeatureRenderer.getInstance().render();
		});
		*///?}
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			boolean frozen = /*? if >1.20.2 {*/ client.tickRateManager().isFrozen(); /*?} else {*/ /*false; *//*?}*/
			PatPatClientConfig config = PatPatClientConfig.getInstance();

			PatPacket packet;
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

			boolean empty = clientPats.isEmpty();

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

			LocalPlayer player = Minecraft.getInstance().player;
			if (!empty && player != null && FabricLoader.getInstance().isDevelopmentEnvironment()) {
				PatPatClientManager.pat(player, PlayerConfig.currentSession());
				ReplayModCompat.onPat(player.getId(), player.getId());
				FlashbackCompat.onPat(player.getId(), player.getId());
			}

			if (!frozen) {
				PatPatClientManager.tickEntities();
			}
		});
	}

	public static void renderPatOnYourself() {
		if (!PatPatClientConfig.getInstance().getVisualConfig().isCameraShackingEnabled()) {
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
		if (player == null || camera.isDetached()) {
			return;
		}

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		//? if >=1.21.2 {
		float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		//?} elif >=1.21 {
		/*float tickDelta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
		*///?} else {
		/*float tickDelta = Minecraft.getInstance().getFrameTime();
		*///?}
		int light = dispatcher.getPackedLightCoords(player, tickDelta);

		PatEntity patEntity = PatPatClientManager.getPatEntity(player);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity, tickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		PatPatClientRenderer.render(new PoseStack(), camera.rotation(), patEntity, player, new Vec3f(0.0F, Mth.lerp(tickDelta, camera.eyeHeightOld, camera.eyeHeight) - 0.2F, 0.0F), tickDelta, light);
	}

	public static RenderResult render(PoseStack matrices, /*? if >=1.19.3 {*/ Quaternionf /*?} else {*/ /*Quaternion *//*?}*/ cameraRotation, @Nullable PatEntity providedPatEntity, @Nullable Entity entity, @Nullable Vec3f overrideOffset, float tickDelta, int light) {
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
		float yOffset = overrideOffset != null ? overrideOffset.getY() : (nameLabelHeight * PatPatClientManager.getAnimationProgress(patEntity, tickDelta)) + 0.11F - frameConfig.offsetY() - config.getVisualConfig().getAnimationOffsets().getY();

		matrices.pushPose();
		matrices.translate(0.0F, yOffset, 0.0F);
		matrices.mulPose(cameraRotation);
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

		PatFeatureRenderer.getInstance().request(animation.getTexture(), matrices.last(), x1, y1, x2, y2, z, u1, v1, u2, v2, light);

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
