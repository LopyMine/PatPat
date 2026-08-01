package net.lopymine.patpat.mixin.tests;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.tests.PatPatTestMode;

//? if >=1.21.9 {
import net.minecraft.client.input.*;
//?}

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

	//? if >=1.21.9 {
	@Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
	private void blockRealKeyPressDuringTests(long window, int key, KeyEvent event, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}

	@Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
	private void blockRealCharTypedDuringTests(long window, CharacterEvent event, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}
	//?} else {
	/*@Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
	private void blockRealKeyPressDuringTests(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}

	@Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
	private void blockRealCharTypedDuringTests(long window, int codePoint, int modifiers, CallbackInfo ci) {
		if (PatPatTestMode.isRealInputBlocked()) {
			ci.cancel();
		}
	}
	*///?}
}
