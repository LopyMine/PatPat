package net.lopymine.patpat.mixin.controlling;

//? if controlling {

import com.blamejared.controlling.client.NewKeyBindsScreen;
import com.blamejared.controlling.platform.IPlatformHelper;
import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IPlatformHelper.class)
public interface IPlatformHelperMixin {

	@Inject(at = @At("HEAD"), method = "handleKeyPress", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(NewKeyBindsScreen screen, Options options, net.minecraft.client.input.KeyEvent event, CallbackInfo ci) {
		int keyCode = event.key();
		int scanCode = event.scancode();
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(screen.selectedKey, screen, keyCode, scanCode, ci::cancel);
	}

}
//?}
