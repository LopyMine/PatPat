package net.lopymine.patpat.mixin.geckolib;

//? if geckolib {
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.lopymine.patpat.client.render.PatPatClientRenderer;

//? if >=1.19.3 {
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.world.entity.Entity;
//?}

//? if >=1.21.9 {
import net.minecraft.client.renderer.state.CameraRenderState;
//?}

@Pseudo
@Mixin(/*? if <1.19.3 {*/ /*targets = "software.bernie.geckolib3.renderer.geo.GeoEntityRenderer" *//*?} else {*/ GeoEntityRenderer.class /*?}*/)
public abstract class GeoEntityRendererMixin {

	//? if >=1.21.10 {
	@Inject(at = @At(value = "HEAD"), method = "submit")
	private void render(net.minecraft.client.renderer.entity.state.EntityRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState, CallbackInfo ci) {
		Entity entity = ((net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent) entityRenderState).patPat$getEntity();
		float partialTick = ((net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent) entityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
	//?} elif >=1.21.2 {
	/*@Inject(at = @At(value = "HEAD"), method = "render")
	private void render(net.minecraft.client.renderer.entity.state.EntityRenderState entityRenderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
		Entity entity = ((net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent) entityRenderState).patPat$getEntity();
		float partialTick = ((net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent) entityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
	*///?} elif >=1.19.3 {
	/*@Inject(at = @At("HEAD"), method = "render")
	private void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
	if (!(entity instanceof LivingEntity livingEntity)) {
			return;
	}
	*///?} else {
	/*@Dynamic
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
	private void render(LivingEntity livingEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
	*///?}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, poseStack, partialTick);
	}
}
//?}

