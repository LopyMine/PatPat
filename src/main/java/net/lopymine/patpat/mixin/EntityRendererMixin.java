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

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.entity.*;

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
        PatAnimation patAnimation = patEntity.getAnimation();
        RenderSystem.setShaderTexture(0, patAnimation.getTexture());
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

        float d = (float) patAnimation.getFrameSize() / patAnimation.getTextureWidth();
        float k = patAnimation.getFrame() * d;
        builder.vertex(matrix,    -p,           0.0F,           0.0F).texture(k, 0.0F).next();
        builder.vertex(matrix,    -p,           0.0F + patSize, 0.0F).texture(k, 1.0F).next();
        builder.vertex(matrix, -p + patSize, 0.0F + patSize, 0.0F).texture(k + d, 1.0F).next();
        builder.vertex(matrix, -p + patSize, 0.0F,           0.0F).texture(k + d, 0.0F).next();

        matrices.pop();

        RenderSystem.enableDepthTest();
        BufferRenderer.drawWithGlobalProgram(builder.end());
//        ci.cancel(); //
    }
}
