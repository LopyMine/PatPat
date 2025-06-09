package net.lopymine.patpat.mixin;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	//? <1.21.2 {
	/*@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void render(LivingEntity livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
	 *///?} else {
	@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	private void render(LivingEntityRenderState livingEntityRenderState, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
	//?}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, matrixStack, tickDelta);
	}
}
