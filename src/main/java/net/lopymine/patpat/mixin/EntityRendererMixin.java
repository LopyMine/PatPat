package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.RenderResult;

//? if <1.21.2 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
*///?} else {
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.renderer.entity.state.*;
//?}

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Shadow
	@Final
	protected EntityRenderDispatcher entityRenderDispatcher;

	/*? if <1.21.2 {*/
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;shouldShowName(Lnet/minecraft/world/entity/Entity;)Z"), method = "render")
	private boolean render(EntityRenderer<?> instance, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) PoseStack matrices, @Local(argsOnly = true) MultiBufferSource provider, @Local(argsOnly = true) int light, @Local(argsOnly = true, ordinal = 1) float tickDelta) {
		boolean bl = original.call(instance, entity);
	*//*?} elif <1.21.9 {*/
	/*@WrapOperation(at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;nameTag:Lnet/minecraft/network/chat/Component;"), method = "render")
	private Component render(EntityRenderState state, Operation<Component> original, @Local(argsOnly = true) PoseStack matrices, @Local(argsOnly = true) MultiBufferSource provider, @Local(argsOnly = true) int light) {
	*///?} else {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submitNameTag(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"), method = "submit")
	private void render(EntityRenderer<?, ?> instance, EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.state.CameraRenderState cameraRenderState, Operation<Void> original, @Local(argsOnly = true) PoseStack matrices) {
	/*?}*/
		//? if >=1.21.2 {
		EntityRenderStateWithParent stateWithParent = (EntityRenderStateWithParent) state;
		Entity entity = stateWithParent.patPat$getEntity();
		float tickDelta = stateWithParent.patPat$getTickDelta();
		//?}
		//? if >=1.21.9 {
		Boolean result = this.render(matrices, state.lightCoords, entity, tickDelta, true);
		if (result != null) {
			original.call(instance, state, poseStack, submitNodeCollector, cameraRenderState);
		}
		//?} elif >=1.21.2 {
		/*return this.render(matrices, light, entity, tickDelta, original.call(state));
		*///?} else {
		/*Boolean result = this.render(matrices, light, entity, tickDelta, bl);
		return result != null && result;
		*///?}
	}

	// original => render, null => cancel
	@Unique
	@Nullable
	private <T> T render(PoseStack matrices, int light, Entity entity, float tickDelta, @SuppressWarnings("all") T original) {
		if (!(entity instanceof LivingEntity)) {
			return original;
		}

		Camera camera = this.entityRenderDispatcher.camera;
		RenderResult result = PatPatClientRenderer.render(
				matrices,
				//? if >=1.21.9 {
				camera == null ? new org.joml.Quaternionf() : camera.rotation(),
				//?} else {
				/*camera.rotation(),
				*///?}
				null,
				entity,
				null,
				tickDelta,
				light
		);

		if (result == RenderResult.RENDERER_SHOULD_CANCEL) {
			return null;
		}
		return original;
	}

	//? if >=1.21.2 {
	@Inject(at = @At(value = "RETURN"), method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;")
	private void getAndUpdateRenderState(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setTickDelta(tickDelta);
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setEntity(entity);
	}
	//?}
}