package net.lopymine.patpat.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

//? <1.21.2 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
*///?} else {
import net.lopymine.patpat.manager.client.PatPatClientRenderer;
import net.lopymine.patpat.manager.client.PatPatClientRenderer.RenderResult;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

//? >1.20.4 {
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
//?}

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Shadow
	@Final
	protected EntityRenderDispatcher dispatcher;

	//? <1.21.2 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"), method = "render")
	private boolean render(EntityRenderer<?> instance, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) MatrixStack matrices, @Local(argsOnly = true) VertexConsumerProvider provider, @Local(argsOnly = true) int light, @Local(argsOnly = true, ordinal = 1) float tickDelta) {
		boolean bl = original.call(instance, entity);
	 *///?} else {
	@Inject(at = @At(value = "HEAD", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), method = "render", cancellable = true)
	private void render(EntityRenderState state, MatrixStack matrices, VertexConsumerProvider provider, int light, CallbackInfo ci) {
	//?}
		//? >=1.21.2 {
		EntityRenderStateWithParent stateWithParent = (EntityRenderStateWithParent) state;
		Entity entity = stateWithParent.patPat$getEntity();
		float tickDelta = stateWithParent.patPat$getTickDelta();
		//?}
		if (!(entity instanceof LivingEntity)) {
			return;
		}

		if (PatPatClientRenderer.render(matrices, provider, this.dispatcher, null, entity, null, tickDelta, light) == RenderResult.RENDERER_SHOULD_CANCEL) {
			ci.cancel();
		}
	}

	//? >=1.21.2 {
	@Inject(at = @At(value = "RETURN"), method = "getAndUpdateRenderState")
	private void getAndUpdateRenderState(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setTickDelta(tickDelta);
		((EntityRenderStateWithParent) cir.getReturnValue()).patPat$setEntity(entity);
	}
	//?}
}