package net.lopymine.patpat.mixin.geckolib;

//? geckolib {
/*import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.renderer.PatAnimationRenderer;
//? =1.16.5 {
/^import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;
^///?} elif >=1.17.1 && <=1.19.2 {
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
//?} else {
/^import software.bernie.geckolib.renderer.GeoEntityRenderer;
^///?}

//? >=1.21.2 {
/^import net.minecraft.client.render.entity.state.EntityRenderState;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;
^///?}

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin {

	@Inject(at=@At(value = "HEAD"), method = "render")
	//? if <=1.16.5 {
	/^private void render(LivingEntity livingEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider bufferIn, int packedLightIn, CallbackInfo ci){
	^///?} elif <1.21.2 {
	private void render(Entity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider bufferIn, int packedLightIn) {
		//?} else {
	
	/^private void render(EntityRenderState entityRenderState, MatrixStack matrixStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) entityRenderState).patPat$getEntity();
		^///?}
		//? if >1.16.5 {
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		//?}
		PatAnimationRenderer.scaleEntityIfPatted(livingEntity, matrixStack);
	}
}
*///?}
