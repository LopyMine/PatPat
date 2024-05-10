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

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.component.*;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        PatPatComponent component = entity.getComponent(PatPatComponents.PATPAT_COMPONENT);
        if (!component.shouldPat()) {
            return;
        }
        Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
        if (cameraEntity == null) {
            return;
        }
        RenderSystem.setShaderTexture(0, PatPat.i("textures/patpat.png"));
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        float patSize = 0.85F;
        float p = patSize / 2.0F;
        matrices.push();
        matrices.translate(0.0F, entity.getNameLabelHeight() - 0.25F, 0.0F);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-patSize, -patSize, patSize);

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float d = (float) (double) 112 / 560;
        float k = component.getFrame() * d;
        builder.vertex(matrix, -p, 0.0F, 0.0F).texture(k, 0.0F).next();
        builder.vertex(matrix, -p, 0.0F + patSize, 0.0F).texture(k, 1.0F).next();
        builder.vertex(matrix, -p + patSize, 0.0F + patSize, 0.0F).texture(k + d, 1.0F).next();
        builder.vertex(matrix, -p + patSize, 0.0F, 0.0F).texture(k + d, 0.0F).next();

        matrices.pop();

        RenderSystem.enableDepthTest();
        BufferRenderer.drawWithGlobalProgram(builder.end());
        ci.cancel(); //
    }
}
