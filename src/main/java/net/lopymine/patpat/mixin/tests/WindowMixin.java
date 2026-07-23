package net.lopymine.patpat.mixin.tests;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.tests.PatPatTestMode;

@Mixin(Window.class)
public class WindowMixin {

	//? if >=1.21.9 {
	@Inject(method = {"onFocus", "onEnter", "onIconify"}, at = @At("HEAD"), cancellable = true)
	//?} else {
	/*@Inject(method = {"onFocus", "onEnter"}, at = @At("HEAD"), cancellable = true)
	*///?}
	private void keepWindowFocusedDuringTests(long window, boolean value, CallbackInfo ci) {
		if (PatPatTestMode.isEnabled()) {
			ci.cancel();
		}
	}
}
