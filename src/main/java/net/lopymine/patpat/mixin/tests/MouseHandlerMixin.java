package net.lopymine.patpat.mixin.tests;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.tests.PatPatTestMode;

//? if >=1.21.9 {
import net.minecraft.client.input.MouseButtonInfo;
//?}

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	//? if >=1.21.9 {
	@Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
	private void blockRealMouseButtonDuringTests(long window, MouseButtonInfo info, int action, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}
	//?} else {
	/*@Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
	private void blockRealMouseButtonDuringTests(long window, int button, int action, int modifiers, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}
	*///?}

	@Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
	private void blockRealMouseMoveDuringTests(long window, double x, double y, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void blockRealMouseScrollDuringTests(long window, double x, double y, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}

	@Inject(method = {"grabMouse", "releaseMouse"}, at = @At("HEAD"), cancellable = true)
	private void keepCursorFreeDuringTests(CallbackInfo ci) {
		if (PatPatTestMode.isEnabled()) {
			ci.cancel();
		}
	}
}
