package net.lopymine.patpat.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.PatPatClientManager;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Shadow
	@Final
	protected EntityRenderDispatcher dispatcher;

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		PatPatClientConfig config = PatPatClient.getConfig();
		if (!config.isModEnabled()) {
			return;
		}
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return;
		}

		CustomAnimationSettingsConfig animation = patEntity.getAnimation();
		FrameConfig frameConfig = animation.getFrameConfig();

		RenderSystem.enableBlend();

		matrices.push();
		matrices.translate(0.0F, livingEntity.getNameLabelHeight() - 0.55F + frameConfig.offsetY() + config.getAnimationOffsetY(), 0.0F);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.scale(-0.85F, -0.85F, 0.85F);

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

		float x1 = -(scaleX / 2F) + frameConfig.offsetX() + config.getAnimationOffsetX();
		float x2 = x1 + scaleX;
		float y1 = -(scaleY / 2F) + frameConfig.offsetY() + config.getAnimationOffsetY();
		float y2 = y1 + scaleY;
		float z = frameConfig.offsetZ() + config.getAnimationOffsetZ();

		float framePercent = (float) 1 / frameConfig.totalFrames();
		float u1 = patEntity.getCurrentFrame() * framePercent;
		float u2 = u1 + framePercent;
		float v1 = 0.0F;
		float v2 = 1.0F;

		Entry peek = matrices.peek();
		Matrix4f matrix4f = peek.getPositionMatrix();
		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(animation.getTexture()));

		buffer.vertex(matrix4f, x1, y1, z).color(255, 255, 255, 255).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0).next();
		buffer.vertex(matrix4f, x1, y2, z).color(255, 255, 255, 255).texture(u1, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0).next();
		buffer.vertex(matrix4f, x2, y2, z).color(255, 255, 255, 255).texture(u2, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0).next();
		buffer.vertex(matrix4f, x2, y1, z).color(255, 255, 255, 255).texture(u2, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0).next();

		matrices.pop();
		RenderSystem.disableBlend();
		if (config.isNicknameHidingEnabled()) {
			ci.cancel();
		}
	}
}
