package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.render.feature.PatFeatureRenderer;
import net.minecraft.client.renderer.feature.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRenderDispatcherMixin {

	@Shadow
	@Final
	private FeatureRendererMap featureRenderers;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void registerPatRenderer(CallbackInfo ci) {
		this.featureRenderers.put(PatFeatureRenderer.TYPE, new PatFeatureRenderer());
	}

}
