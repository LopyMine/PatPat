package net.lopymine.patpat.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.entity.*;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        PatEntity patEntity = PatPatClient.getPatEntity(livingEntity);
        if (patEntity == null) {
            return;
        }

        PatAnimation patAnimation = patEntity.getAnimation();
        int duration = patAnimation.getDuration();
        long time = patAnimation.getTimeOfStart();
        long timeNow = Util.getMeasuringTimeMs();
        if (timeNow > (time + duration)) {
            PatPatClient.removePatEntity(patEntity);
            return;
        }

        // TODO
        //  Честно, я хз как это правильно посчитать и сделать, но нужно сделать чтобы относительно {animationProgress}
        //  в patAnimation устанавливался определённый кадр, вероятно для этого потребуются методы:
        //  patAnimation.getTotalFrames(), patAnimation.getFrame(), patAnimation.getFrameSize()
        //  Это важно т.к. в кастомных текстурах количество кадров может быть разным!!!

        float animationProgress = MathHelper.clamp((float) (timeNow - time) / duration, 0.0F, 1.0F);
        animationProgress = (float) (1 - Math.pow(1 - animationProgress, 3));
        if (animationProgress < 0.2) {
            patAnimation.setFrame(0);
        } else if (animationProgress < 0.4) {
            patAnimation.setFrame(1);
        } else if (animationProgress < 0.6) {
            patAnimation.setFrame(2);
        } else if (animationProgress < 0.8) {
            patAnimation.setFrame(3);
        } else {
            patAnimation.setFrame(4);
        }

        float range = 0.425F / livingEntity.getHeight();
        float animation = ((float) ((1 - range) + range * (1 - Math.sin(animationProgress * Math.PI))));

        matrixStack.scale(1, animation, 1);
    }
}
