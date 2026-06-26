package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBindsScreen.class)
public class KeyBindsScreenMixin implements ScreenWithPatPatKeybinding {

	@Shadow
	@Nullable
	public KeyMapping selectedKey;

	@Shadow
	private KeyBindsList keyBindsList;

	@Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(net.minecraft.client.input.KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		int keyCode = event.key();
		int scanCode = event.scancode();
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(this.selectedKey, (KeyBindsScreen) (Object) (this), keyCode, scanCode, () -> cir.setReturnValue(false));
	}

	@Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
	private void handlePatPatKeybindingOnMouseClick(net.minecraft.client.input.MouseButtonEvent event, boolean bl, CallbackInfoReturnable<Boolean> cir) {
		int button = event.button();
		PatPatClientKeybindingManager.handlePatPatKeybindingOnMouseClick(this.selectedKey, (KeyBindsScreen) (Object) (this), button, () -> cir.setReturnValue(false));
	}

	@Override
	public void patPat$onKeyReleased() {
		if (this.selectedKey instanceof PatPatKeybinding keybinding && keybinding.isBinding()) {
			keybinding.sendBindingKeys();
			this.selectedKey = null;
			this.keyBindsList.refreshEntries();
		}
	}
}
