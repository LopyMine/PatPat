package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.*;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.InputConstants;

import net.lopymine.patpat.client.manager.PatPatClientManager;

@Mixin(KeyMapping.class)
public class KeybindingMixin {

	private KeybindingMixin() {
		throw new IllegalStateException("Mixin class");
	}

	@Inject(at = @At("HEAD"), method = "click", cancellable = true)
	private static void cancelClickForPatPatKeybinding(InputConstants.Key key, CallbackInfo ci) {
		PatPatKeybinding patKeybinding = PatPatClientKeybindingManager.getPatKeybinding();
		if (patKeybinding == null) {
			return;
		}
		if (patKeybinding.onKeyAction(key, true) && PatPatClientManager.canPat()) {
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "set", cancellable = true)
	private static void processSetForPatPatKeybinding(InputConstants.Key key, boolean pressed, CallbackInfo ci) {
		PatPatKeybinding patKeybinding = PatPatClientKeybindingManager.getPatKeybinding();
		if (patKeybinding == null) {
			return;
		}
		if (patKeybinding.onKeyAction(key, pressed) && PatPatClientManager.canPat()) {
			ci.cancel();
		}
	}

}
