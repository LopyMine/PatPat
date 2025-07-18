package net.lopymine.patpat.mixin;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.RenderResult;

//? <1.21.2 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
*///?} else {
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//?}

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Shadow
	@Final
	protected EntityRenderDispatcher entityRenderDispatcher;

	//? <1.21.2 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;shouldShowName(Lnet/minecraft/world/entity/Entity;)Z"), method = "render")
	private boolean render(EntityRenderer<?> instance, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) PoseStack matrices, @Local(argsOnly = true) MultiBufferSource provider, @Local(argsOnly = true) int light, @Local(argsOnly = true, ordinal = 1) float tickDelta) {
		boolean bl = original.call(instance, entity);
	*///?} else {
	@Inject(at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;renderNameTag(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), method = "render", cancellable = true)
	private void render(EntityRenderState state, PoseStack matrices, MultiBufferSource provider, int light, CallbackInfo ci) {
	//?}

		//? >=1.21.2 {
		EntityRenderStateWithParent stateWithParent = (EntityRenderStateWithParent) state;
		Entity entity = stateWithParent.patPat$getEntity();
		float tickDelta = stateWithParent.patPat$getTickDelta();
		//?}

		if (!(entity instanceof LivingEntity)) {
			return /*? if <1.21.2 {*/ /*bl *//*?}*/;
		}

		RenderResult result = PatPatClientRenderer.render(matrices, provider, this.entityRenderDispatcher, null, entity, null, tickDelta, light);
		//? if >=1.21.2 {
		if (result == RenderResult.RENDERER_SHOULD_CANCEL) {
			ci.cancel();
		}
		//?} else {
		/*if (result == RenderResult.RENDERER_SHOULD_CANCEL) {
			return false;
		}
		return bl;
		*///?}
	}

	//? >=1.21.2 {
	@Inject(at = @At(value = "RETURN"), method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;")
	private void getAndUpdateRenderState(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setTickDelta(tickDelta);
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setEntity(entity);
	}
	//?}
}