package net.lopymine.patpat.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.component.*;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        PatPatComponent component = livingEntity.getComponent(PatPatComponents.PATPAT_COMPONENT);
        if (!component.shouldPat()) {
            return;
        }
        int duration = component.getDuration();
        long time = component.getTimeOfStart();
        long timeNow = Util.getMeasuringTimeMs();
        if (timeNow > (time + duration)) {
            component.unPat();
            return;
        }

        float animationProgress = MathHelper.clamp((float) (timeNow - time) / duration, 0.0F, 1.0F);
        animationProgress = (float) (1 - Math.pow(1 - animationProgress, 3));

        if (animationProgress < 0.2) {
            component.setFrame(0);
        } else if (animationProgress < 0.4) {
            component.setFrame(1);
        } else if (animationProgress < 0.6) {
            component.setFrame(2);
        } else if (animationProgress < 0.8) {
            component.setFrame(3);
        } else {
            component.setFrame(4);
        }

        float range = 0.425F / livingEntity.getHeight();
        float animation = ((float) ((1 - range) + range * (1 - Math.sin(animationProgress * Math.PI))));

        matrixStack.scale(1, animation, 1);
    }
}
