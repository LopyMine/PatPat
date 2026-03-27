package net.lopymine.patpat.mixin.controlling;

//? if >=1.21 && controlling {

/*import com.blamejared.controlling.client.NewKeyBindsScreen;
import com.blamejared.controlling.platform.IPlatformHelper;
import net.lopymine.patpat.client.keybinding.*;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(IPlatformHelper.class)
public interface IPlatformHelperMixin {

	@Inject(at = @At("HEAD"), method = "handleKeyPress", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(NewKeyBindsScreen screen, Options options, /^? if >=1.21.9 {^//^net.minecraft.client.input.KeyEvent event ^//^?} else {^/ net.minecraft.client.input.KeyEvent event /^?}^/, CallbackInfo ci) {
		//? if >=1.21.9 {
		/^int keyCode = event.key();
		int scanCode = event.scancode();
		^///?}
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(screen.selectedKey, screen, keyCode, scanCode, ci::cancel);
	}

}
*///?}
