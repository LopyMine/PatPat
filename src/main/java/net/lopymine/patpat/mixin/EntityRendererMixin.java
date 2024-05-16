package net.lopymine.patpat.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.AnimationConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Shadow
	@Final
	protected EntityRenderDispatcher dispatcher;

	@Inject(at = @At("HEAD"), method = "render")
	private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		PatEntity patEntity = PatPatClient.getPatEntity(livingEntity);
		if (patEntity == null) {
			return;
		}
		Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
		if (cameraEntity == null) {
			return;
		}
		AnimationConfig animationConfig = patEntity.getAnimation();
		RenderSystem.setShaderTexture(0, animationConfig.getTexture());
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

		float patSize = 0.85F;
		float p = patSize / 2.0F;
		matrices.push();
		matrices.translate(0.0F, livingEntity.getNameLabelHeight() - 0.25F, 0.0F);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.scale(-patSize, -patSize, patSize);

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		float d = (float) animationConfig.getFrameSize() / animationConfig.getTextureWidth();
		float k = patEntity.getFrame() * d;
		builder.vertex(matrix, -p, 0.0F, 0.0F).texture(k, 0.0F).next();
		builder.vertex(matrix, -p, patSize, 0.0F).texture(k, 1.0F).next();
		builder.vertex(matrix, -p + patSize, patSize, 0.0F).texture(k + d, 1.0F).next();
		builder.vertex(matrix, -p + patSize, 0.0F, 0.0F).texture(k + d, 0.0F).next();

		matrices.pop();

		RenderSystem.enableDepthTest();
		BufferRenderer.drawWithGlobalProgram(builder.end());
	}
}
