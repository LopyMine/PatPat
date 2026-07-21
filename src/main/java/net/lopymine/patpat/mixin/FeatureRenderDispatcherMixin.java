package net.lopymine.patpat.mixin;

//? if >=1.21.9 {

import net.lopymine.patpat.client.render.feature.PatFeatureRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRenderDispatcherMixin {

	@Shadow @Final private BufferSource bufferSource;

	//? if >=26.1 {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer;renderTranslucent(Lnet/minecraft/client/renderer/SubmitNodeCollection;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/gui/Font;)V"), method = "renderTranslucentFeatures")
	private void renderPatFeature(CallbackInfo ci) {
		PatFeatureRenderer.getInstance().render(this.bufferSource);
	}
	//?} else {
	/*@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer;render(Lnet/minecraft/client/renderer/SubmitNodeCollection;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/gui/Font;)V"), method = "renderAllFeatures")
		private void renderPatFeature(CallbackInfo ci) {
			PatFeatureRenderer.getInstance().render(this.bufferSource);
		}
	*///?}

}
//?}
