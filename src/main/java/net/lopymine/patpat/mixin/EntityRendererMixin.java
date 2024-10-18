package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

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

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"), method = "render")
	private boolean render(EntityRenderer<?> instance, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) MatrixStack matrices, @Local(argsOnly = true) VertexConsumerProvider provider, @Local(argsOnly = true) int light, @Local(argsOnly = true, ordinal = 1) float tickDelta) {
		boolean bl = original.call(instance, entity);

		PatPatClientConfig config = PatPatClient.getConfig();
		if (!config.isModEnabled()) {
			return bl;
		}
		if (!(entity instanceof LivingEntity livingEntity)) {
			return bl;
		}
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return bl;
		}

		//? <=1.20.4 {
		/*float nameLabelHeight = livingEntity.getHeight() + 0.5F;
		*///?} else {
		net.minecraft.util.math.Vec3d vec3d = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getYaw(tickDelta));
		float nameLabelHeight = vec3d != null ? (float) vec3d.y + 0.5F : 0.0F;
		//?}

		int numberToMirrorTexture = /*? >=1.21 {*/1/*?} else {*/ /*-1*//*?}*/;

		CustomAnimationSettingsConfig animation = patEntity.getAnimation();
		FrameConfig frameConfig = animation.getFrameConfig();

		RenderSystem.enableBlend();

		matrices.push();
		matrices.translate(0.0F, nameLabelHeight - 0.55F - frameConfig.offsetY() - config.getAnimationOffsetY(), 0.0F);
		matrices.multiply(this.dispatcher.getRotation());
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

		float x1 = -(scaleX / 2F) + frameConfig.offsetX() + config.getAnimationOffsetX();
		float x2 = x1 + scaleX;
		float y1 = -(scaleY / 2F);
		float y2 = y1 + scaleY;
		float z = -(frameConfig.offsetZ() + config.getAnimationOffsetZ());

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
		RenderSystem.disableBlend();
		return !config.isNicknameHidingEnabled();
	}
}
