package net.lopymine.patpat.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.client.config.*;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.config.sub.PatPatClientVisualConfig;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.client.render.feature.*;
import net.lopymine.patpat.client.render.feature.PatFeatureRenderer.Submit;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.compat.flashback.FlashbackCompat;
import net.lopymine.patpat.compat.replaymod.ReplayModCompat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.lopymine.patpat.extension.VertexConsumerExtension;
import net.minecraft.client.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;


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

	public static void register() {
		ClientMultiLoader.getInstance().registerAfterWorldTickListener((level) -> {
			boolean frozen = level.tickRateManager().isFrozen();
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

			boolean empty = clientPats.isEmpty();

			while ((packet = clientPats.poll()) != null) {
				LocalPlayer player = packet.player();
				LivingEntity pattedEntity = packet.pattedEntity();
				PlayerConfig playerConfig = packet.playerConfig();

				ClientMultiLoader.getInstance().sendPacketToServer(PatPatClientPacketManager.getPatPacket(pattedEntity));
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
			if (!empty && player != null && PatPatDebugConfig.DEBUG_ENABLED && PatPatDebugConfig.getInstance().isSelfPat()) {
				PatPatClientManager.pat(player, PlayerConfig.currentSession());
				ReplayModCompat.onPat(player.getId(), player.getId());
				FlashbackCompat.onPat(player.getId(), player.getId());
			}

			if (!frozen) {
				PatPatClientManager.tickEntities();
			}
		});
	}

	public static void submitPatOnYourself(SubmitNodeCollector collector) {
		if (!PatPatClientConfig.getInstance().getVisualConfig().isCameraShackingEnabled()) {
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();
		if (player == null || camera.isDetached()) {
			return;
		}

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		int light = dispatcher.getPackedLightCoords(player, tickDelta);

		PatEntity patEntity = PatPatClientManager.getPatEntity(player);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity, tickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		PatPatClientRenderer.submit(collector, new PoseStack(), camera.rotation(), patEntity, player, new Vec3f(0.0F, Mth.lerp(tickDelta, camera.eyeHeightOld, camera.eyeHeight) - 0.2F, 0.0F), tickDelta, light);
	}

	public static RenderResult submit(SubmitNodeCollector collector, PoseStack matrices, Quaternionf cameraRotation, @Nullable PatEntity providedPatEntity, @Nullable Entity entity, @Nullable Vec3f overrideOffset, float tickDelta, int light) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return RenderResult.FAILED;
		}

		PatEntity patEntity = providedPatEntity;
		if (patEntity == null && entity instanceof LivingEntity livingEntity) {
			patEntity = PatPatClientManager.getPatEntity(livingEntity);
		}

		if (patEntity == null) {
			return RenderResult.FAILED;
		}

		int numberToMirrorTexture = 1;

		CustomAnimationSettingsConfig animation = patEntity.getAnimation();
		FrameConfig frameConfig = animation.getFrameConfig();
		enableBlend();
		net.minecraft.world.phys.Vec3 vec3d = entity != null ? entity.getAttachments().getNullable(net.minecraft.world.entity.EntityAttachment.NAME_TAG, 0, entity.getViewYRot(tickDelta)) : null;
		float nameLabelHeight = vec3d != null ? (float) vec3d.y : 0.0F;
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

		PatFeatureRenderer.submit(collector, new Submit(animation.getTexture(), matrices.last().copy(), x1, y1, x2, y2, z, u1, v1, u2, v2, light));

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

	private static void enableBlend() {
	}

	private static void disableBlend() {
	}

	public enum RenderResult {
		RENDERED,
		RENDERER_SHOULD_CANCEL,
		FAILED
	}

	public record PacketPat(LivingEntity pattedEntity, PlayerConfig playerConfig, LocalPlayer player,
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

}
