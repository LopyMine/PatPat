package net.lopymine.patpat.mixin;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.lopymine.patpat.client.render.PatPatClientRenderer;

//? if >=1.21.2 {
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
//?}

@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin {

	//? if >=1.21.9 {
	/*@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V")
	private void makeDragonPattableBecauseWhyNot(net.minecraft.client.renderer.entity.state.EnderDragonRenderState enderDragonRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.state.CameraRenderState cameraRenderState, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) enderDragonRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) enderDragonRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
	*///?} elif >=1.21.2 {
	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
	private void makeDragonPattableBecauseWhyNot(net.minecraft.client.renderer.entity.state.EnderDragonRenderState enderDragonRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) enderDragonRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) enderDragonRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
	//?} else {
	/*@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
	private void makeDragonPattableBecauseWhyNot(net.minecraft.world.entity.boss.enderdragon.EnderDragon livingEntity, float f, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
	*///?}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, poseStack, tickDelta);
	}

}
