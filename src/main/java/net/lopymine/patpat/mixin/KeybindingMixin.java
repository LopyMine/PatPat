package net.lopymine.patpat.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.InputConstants;

import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.lopymine.patpat.client.manager.PatPatClientManager;

@Mixin(KeyMapping.class)
public class KeybindingMixin {

	@Inject(at = @At("HEAD"), method = "click", cancellable = true)
	private static void cancelClickForPatPatKeybinding(InputConstants.Key key, CallbackInfo ci) {
		if (PatPatClientKeybindingManager.PAT_KEYBINDING.onKeyAction(key, true) && PatPatClientManager.canPat()) {
			PatPatClientManager.requestPat();
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "set", cancellable = true)
	private static void processSetForPatPatKeybinding(InputConstants.Key key, boolean pressed, CallbackInfo ci) {
		if (PatPatClientKeybindingManager.PAT_KEYBINDING.onKeyAction(key, pressed) && PatPatClientManager.canPat()) {
			PatPatClientManager.requestPat();
			ci.cancel();
		}
	}

}
