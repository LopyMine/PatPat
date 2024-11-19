package net.lopymine.patpat.mixin;

// TODO Отключить класс из версий, в которых GeckoLib не создан (например 1.21.2)

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import net.lopymine.patpat.renderer.PatAnimationRenderer;


//? >=1.21.2 {
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
//?}

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin {

	//? <1.21.2 {
	/*@Inject(at=@At(value = "HEAD"), method = "render")
	private void render(Entity entity, float entityYaw, float partialTick, MatrixStack matrixStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
		*///?} else {
	@Inject(at = @At(value = "HEAD"), method = "render")
	private void render(EntityRenderState entityRenderState, MatrixStack matrixStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) entityRenderState).patPat$getEntity();
		//?}
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		PatAnimationRenderer.scaleEntityIfPatted(livingEntity, matrixStack);
	}
}
