package net.lopymine.patpat.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 1039)
public class LevelRendererMixin {

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;submitEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
					shift = Shift.AFTER
			),
			method = "submitFeatures"
	)
	private void renderPatOnYourself(CallbackInfo ci, @Local(argsOnly = true) SubmitNodeCollector collector) {
		PatPatClientRenderer.submitPatOnYourself(collector);
	}

}
