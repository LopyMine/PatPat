package net.lopymine.patpat.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.PatPatClientManager;

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
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return;
		}
		Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
		if (cameraEntity == null) {
			return;
		}

		AnimationConfig animation = patEntity.getAnimation();
		FrameConfig frameConfig = animation.frameConfig();
//
		RenderSystem.setShaderTexture(0, animation.texture());
		RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
		RenderSystem.enableBlend();

		BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

		matrices.push();
		matrices.translate(0.0F, livingEntity.getNameLabelHeight() - 0.35F, 0.0F);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.scale(-0.85F, -0.85F, 0.85F);

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		float textureWidth = frameConfig.frameWidth() * frameConfig.totalFrames(); // all texture width
		float textureFrameWidth = frameConfig.frameWidth() / textureWidth; // one frame size, for example: 0.33

		float worldFrameWidth = (float) frameConfig.frameWidth() / FrameConfig.DEFAULT_FRAME.frameWidth() - 0.15F; // frame width in !! world !!, default: 1.0F
		float worldFrameHeight = (float) frameConfig.frameHeight() / FrameConfig.DEFAULT_FRAME.frameWidth() - 0.15F; // frame height in !! world !!, default: 1.0F

		float x1 = -(worldFrameWidth / 2F) + frameConfig.offsetX();
		float x2 = x1 + worldFrameWidth;

		float y1 = -(worldFrameHeight / 2F) - frameConfig.offsetY();
		float y2 = y1 + worldFrameHeight;

		float u1 = patEntity.getCurrentFrame() * textureFrameWidth;
		float u2 = u1 + textureFrameWidth;
		float v1 = 0.0F;
		float v2 = 1.0F;

		builder.vertex(matrix, x1, y1, frameConfig.offsetZ()).color(1.0F, 1.0F, 1.0F, 1.0F).light(light).texture(u1, v1).next();
		builder.vertex(matrix, x1, y2, frameConfig.offsetZ()).color(1.0F, 1.0F, 1.0F, 1.0F).light(light).texture(u1, v2).next();
		builder.vertex(matrix, x2, y2, frameConfig.offsetZ()).color(1.0F, 1.0F, 1.0F, 1.0F).light(light).texture(u2, v2).next();
		builder.vertex(matrix, x2, y1, frameConfig.offsetZ()).color(1.0F, 1.0F, 1.0F, 1.0F).light(light).texture(u2, v1).next();

		matrices.pop();

		RenderSystem.enableDepthTest();
		BufferRenderer.drawWithGlobalProgram(builder.end());
		RenderSystem.disableBlend();
	}
}
