package net.lopymine.patpat.mixin;

//? if >=1.21.9 {

import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.feature.PatFeatureRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 1039)
public class LevelRendererMixin {

	//? if >=26.1 {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderTranslucentFeatures()V"), method = "lambda$addMainPass$0")
	private void markLevelRendering0(CallbackInfo ci) {
		PatFeatureRenderer.getInstance().setRenderingLevel(true);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderTranslucentFeatures()V", shift = Shift.AFTER), method = "lambda$addMainPass$0")
	private void markLevelRendering1(CallbackInfo ci) {
		PatFeatureRenderer.getInstance().setRenderingLevel(false);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", shift = Shift.AFTER), method = "lambda$addMainPass$0")
	private void renderPatOnYourself(CallbackInfo ci) {
		PatPatClientRenderer.renderPatOnYourself();
	}
	//?} else {
	/*@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/state/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", shift = Shift.AFTER), method = "method_62214")
		private void renderPatOnYourself(CallbackInfo ci) {
			PatPatClientRenderer.renderPatOnYourself();
		}
	*///?}

}
//?}
