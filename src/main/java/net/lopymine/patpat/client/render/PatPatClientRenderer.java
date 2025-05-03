package net.lopymine.patpat.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;

//? <=1.21.4 {
import com.mojang.blaze3d.systems.RenderSystem;
/*?} else {*/
/*import com.mojang.blaze3d.opengl.GlStateManager;
 *//*?}*/

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.*;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.common.config.vector.Vec3f;
import net.lopymine.patpat.entity.PatEntity;

import org.jetbrains.annotations.Nullable;
public class PatPatClientRenderer {

	public static void register() {
		WorldRenderEvents.AFTER_ENTITIES.register(PatPatClientRenderer::renderPatOnYourself);
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			//? >1.20.2 {
			if (client.getTickManager().isFrozen()) {
				return;
			}
			//?}
			PatPatClientManager.tickEntities();
		});
	}

	private static void renderPatOnYourself(WorldRenderContext context) {
		if (!PatPatClientConfig.getInstance().getVisualConfig().isCameraShackingEnabled()) {
			return;
		}

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		VertexConsumerProvider consumers = context.consumers();
		MatrixStack matrices = context.matrixStack();
		Camera camera = context.camera();

		if (player == null || matrices == null || consumers == null || camera.isThirdPerson()) {
			return;
		}

		EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		float tickDelta = context./*? if >=1.21 {*/ tickCounter().getTickDelta(false); /*?} else {*/ /*tickDelta(); *//*?}*/
		int light = dispatcher.getLight(player, tickDelta);

		PatEntity patEntity = PatPatClientManager.getPatEntity(player);
		if (patEntity == null) {
			return;
		}

		if (PatPatClientManager.expired(patEntity, tickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return;
		}

		PatPatClientRenderer.render(matrices, consumers, dispatcher, patEntity, player, new Vec3f(0.0F, MathHelper.lerp(tickDelta, camera.lastCameraY, camera.cameraY) - 0.2F, 0.0F), tickDelta, light);
	}

	public static RenderResult render(MatrixStack matrices, VertexConsumerProvider provider, EntityRenderDispatcher dispatcher, @Nullable PatEntity providedPatEntity, @Nullable Entity entity, @Nullable Vec3f overrideOffset, float tickDelta, int light) {
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
		Vec3d vec3d = entity != null ? entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getYaw(tickDelta)) : null;
		float nameLabelHeight = vec3d != null ? (float) vec3d.y: 0.0F;
		//?}

		matrices.push();
		matrices.translate(
				0.0F,
				overrideOffset != null ? overrideOffset.getY() : (nameLabelHeight * PatPatClientManager.getAnimationProgress(patEntity, tickDelta)) + 0.11F - frameConfig.offsetY() - config.getVisualConfig().getAnimationOffsets().getY(),
				0.0F
		);
		matrices.multiply(dispatcher.getRotation());
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

		Entry peek = matrices.peek();
		/*? >=1.19.3 {*/org.joml.Matrix4f/*?} else {*/ /*net.minecraft.util.math.Matrix4f*//*?}*/ matrix4f = peek./*? <=1.17.1 {*//*getModel()*//*?} else {*/getPositionMatrix()/*?}*/;
		VertexConsumer buffer = provider.getBuffer(RenderLayer.getEntityTranslucent(animation.getTexture()));

		buffer.vertex(matrix4f, x1, y1, z).color(255, 255, 255, 255).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/
		buffer.vertex(matrix4f, x1, y2, z).color(255, 255, 255, 255).texture(u1, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/
		buffer.vertex(matrix4f, x2, y2, z).color(255, 255, 255, 255).texture(u2, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/
		buffer.vertex(matrix4f, x2, y1, z).color(255, 255, 255, 255).texture(u2, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0)/*? <=1.20.6 {*//*.next();*//*?} else {*/; /*?}*/

		matrices.pop();
		disableBlend();
		if (config.getVisualConfig().isHidingNicknameEnabled()) {
			return RenderResult.RENDERER_SHOULD_CANCEL;
		}
		return RenderResult.RENDERED;
	}

	public static void scaleEntityIfPatted(LivingEntity livingEntity, MatrixStack matrixStack, float tickDelta){
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
