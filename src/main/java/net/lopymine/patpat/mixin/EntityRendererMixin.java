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
		FrameConfig frameConfig = animation.frameConfig();

		RenderSystem.enableBlend();

		matrices.push();
		matrices.translate(0.0F, livingEntity.getNameLabelHeight() - 0.55F + ((float) frameConfig.animationOffsetY()) + ((float) config.getAnimationOffsetY()), 0.0F);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.scale(-0.85F, -0.85F, 0.85F);

		float textureWidth = frameConfig.frameWidth() * frameConfig.totalFrames(); // all texture width
		float textureFrameWidth = frameConfig.frameWidth() / textureWidth; // one frame size, for example: 0.33

		float worldFrameWidth = (float) frameConfig.frameWidth() / FrameConfig.DEFAULT_FRAME.frameWidth(); // frame width in !! world !!, default: 1.0F
		float worldFrameHeight = (float) frameConfig.frameHeight() / FrameConfig.DEFAULT_FRAME.frameWidth(); // frame height in !! world !!, default: 1.0F

		float x1 = -(worldFrameWidth / 2F) + ((float) frameConfig.animationOffsetX()) + ((float) config.getAnimationOffsetX());
		float x2 = x1 + worldFrameWidth;

		float y1 = -(worldFrameHeight / 2F);
		float y2 = y1 + worldFrameHeight;

		float z = ((float) frameConfig.animationOffsetZ()) + ((float) config.getAnimationOffsetZ());

		float u1 = patEntity.getCurrentFrame() * textureFrameWidth;
		float u2 = u1 + textureFrameWidth;
		float v1 = 0.0F;
		float v2 = 1.0F;

		Entry peek = matrices.peek();
		Matrix4f matrix4f = peek.getPositionMatrix();
		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(animation.texture()));

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
