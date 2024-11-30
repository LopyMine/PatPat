package net.lopymine.patpat.mixin.geckolib;

//? !(=1.17 || =1.18 || =1.18.1 || =1.19 || =1.21.2) {
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.renderer.PatAnimationRenderer;
//? =1.16.5 {
/*import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;
*///?} elif >=1.17.1 && <=1.19.2 {
/*import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
*///?} else {
import software.bernie.geckolib.renderer.GeoEntityRenderer;
//?}

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

//?}
