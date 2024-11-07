package net.lopymine.patpat.mixin;

import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.PatPatClientManager;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

//	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V"))
//	private void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
//		if (patEntity == null) {
//			return;
//		}
//
//		if (PatPatClientManager.expired(patEntity)) {
//			PatPatClientManager.removePatEntity(patEntity);
//			return;
//		}
//
//		matrixStack.scale(1F, PatPatClientManager.getAnimationProgress(patEntity), 1F);
//	}

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void render(LivingEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        Entity entity = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
        if (patEntity == null) {
            return;
        }

        if (PatPatClientManager.expired(patEntity)) {
            PatPatClientManager.removePatEntity(patEntity);
            return;
        }

        matrixStack.scale(1F, PatPatClientManager.getAnimationProgress(patEntity), 1F);
    }
}
