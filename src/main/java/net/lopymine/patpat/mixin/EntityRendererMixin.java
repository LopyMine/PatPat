package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.RenderResult;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Shadow
	@Final
	protected EntityRenderDispatcher entityRenderDispatcher;

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"), method = "submit")
	private void submitPat(EntityRenderer<?, ?> instance, EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, Operation<Void> original, @Local(argsOnly = true) PoseStack matrices) {
		EntityRenderStateWithParent stateWithParent = (EntityRenderStateWithParent) state;
		Entity entity = stateWithParent.patPat$getEntity();
		float tickDelta = stateWithParent.patPat$getTickDelta();

		Boolean result = this.submit(submitNodeCollector, matrices, state.lightCoords, entity, tickDelta, true);
		if (result != null) {
			original.call(instance, state, poseStack, submitNodeCollector, cameraRenderState);
		}
	}

	// original => render, null => cancel
	@Unique
	@Nullable
	private <T> T submit(SubmitNodeCollector collector, PoseStack matrices, int light, Entity entity, float tickDelta, @SuppressWarnings("all") T original) {
		if (!(entity instanceof LivingEntity)) {
			return original;
		}

		Camera camera = this.entityRenderDispatcher.camera;
		RenderResult result = PatPatClientRenderer.submit(
				collector,
				matrices,
				camera == null ? new org.joml.Quaternionf() : camera.rotation(),
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

	@Inject(at = @At(value = "RETURN"), method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;")
	private void getAndUpdateRenderState(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setTickDelta(tickDelta);
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setEntity(entity);
	}
}