package net.lopymine.patpat.mixin;

//? if >=1.21.9 {

/*import net.lopymine.patpat.client.render.feature.PatFeatureRenderer;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRenderDispatcherMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer;render(Lnet/minecraft/client/renderer/SubmitNodeCollection;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/gui/Font;)V"), method = "renderAllFeatures")
	private void renderPatFeature(CallbackInfo ci) {
		PatFeatureRenderer.getInstance().render();
	}

}
*///?}
