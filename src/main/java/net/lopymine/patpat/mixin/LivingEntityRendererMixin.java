package net.lopymine.patpat.mixin;

import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

//? if >=1.21.2 {
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
//?}

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	//? if >=1.21.9 {
	@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	private void render(LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.state.CameraRenderState cameraRenderState, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
	//?} elif >=1.21.2 {
	/*@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	private void render(LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) livingEntityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
	}*///?} else {
	/*@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
		private void render(LivingEntity livingEntity, float f, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
	*///?}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, poseStack, tickDelta);
	}
}
