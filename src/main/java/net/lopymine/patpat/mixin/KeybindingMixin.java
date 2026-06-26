package net.lopymine.patpat.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public class KeybindingMixin {

	private KeybindingMixin() {
		throw new IllegalStateException("Mixin class");
	}

	@Inject(at = @At("HEAD"), method = "click", cancellable = true)
	private static void cancelClickForPatPatKeybinding(InputConstants.Key key, CallbackInfo ci) {
		if (PatPatClientKeybindingManager.getPatKeybinding().onKeyAction(key, true) && PatPatClientManager.canPat()) {
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "set", cancellable = true)
	private static void processSetForPatPatKeybinding(InputConstants.Key key, boolean pressed, CallbackInfo ci) {
		if (PatPatClientKeybindingManager.getPatKeybinding().onKeyAction(key, pressed) && PatPatClientManager.canPat()) {
			ci.cancel();
		}
	}

}
