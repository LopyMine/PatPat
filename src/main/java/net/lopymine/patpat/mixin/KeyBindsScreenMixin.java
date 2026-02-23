package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.controls.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;

import org.jetbrains.annotations.Nullable;

@Mixin(KeyBindsScreen.class)
public class KeyBindsScreenMixin implements ScreenWithPatPatKeybinding {

	@Shadow @Nullable public KeyMapping selectedKey;

	//? if >=1.19.4 {
	@Shadow
	private KeyBindsList keyBindsList;
	//?}

	@Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		//? if >=1.21.9 {
		/*int keyCode = event.key();
		int scanCode = event.scancode();
		*///?}
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(this.selectedKey, (KeyBindsScreen) (Object) (this), keyCode, scanCode, () -> cir.setReturnValue(false));
	}

	@Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
	private void handlePatPatKeybindingOnMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		//? if >=1.21.9 {
		/*int button = event.button();
		*///?}
		PatPatClientKeybindingManager.handlePatPatKeybindingOnMouseClick(this.selectedKey, (KeyBindsScreen) (Object) (this), button, () -> cir.setReturnValue(false));
	}

	@Override
	public void patPat$onKeyReleased() {
		if (this.selectedKey instanceof PatPatKeybinding keybinding && keybinding.isBinding()) {
			keybinding.sendBindingKeys();
			this.selectedKey = null;
			//? if >=1.19.4 {
			this.keyBindsList.refreshEntries();
			//?}
		}
	}
}
