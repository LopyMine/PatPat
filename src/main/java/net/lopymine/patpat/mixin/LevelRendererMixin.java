package net.lopymine.patpat.mixin;

//? if >=1.21.9 {

import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/state/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V", shift = Shift.AFTER), method = "method_62214")
	private void renderPatOnYourself(CallbackInfo ci) {
		PatPatClientRenderer.renderPatOnYourself();
	}

}
//?}
