package net.lopymine.patpat.mixin.controlling;

//? if >=1.21 && controlling {

import com.blamejared.controlling.client.NewKeyBindsScreen;
import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NewKeyBindsScreen.class)
public abstract class NewKeyBindsScreenModernMixin extends KeyBindsScreen {

	public NewKeyBindsScreenModernMixin(Screen screen, Options options) {
		super(screen, options);
	}

	@Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(/*? if >=1.21.9 {*/net.minecraft.client.input.KeyEvent event/*?} else {*/ /*int keyCode, int scanCode, int modifiers*//*?}*/, CallbackInfoReturnable<Boolean> cir) {
		//? if >=1.21.9 {
		int keyCode = event.key();
		int scanCode = event.scancode();
		//?}
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(this.selectedKey, this, keyCode, scanCode, () -> cir.setReturnValue(false));
	}

}
//?}
