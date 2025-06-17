package net.lopymine.patpat.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.InputConstants;

import net.lopymine.patpat.client.keybinding.PatPatKeybinding;
import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;

import org.jetbrains.annotations.Nullable;

@Mixin(KeyBindsScreen.class)
public class KeyBindsScreenMixin implements ScreenWithPatPatKeybinding {

	@Shadow @Nullable public KeyMapping selectedKey;

	@Shadow private KeyBindsList keyBindsList;

	@Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		if (this.selectedKey instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(InputConstants.getKey(keyCode, scanCode));
			if (bl) {
				keybinding.sendBindingKeys();
				this.selectedKey = null;
			}
			this.keyBindsList.refreshEntries();
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
	private void handlePatPatKeybindingOnMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (this.selectedKey instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(InputConstants.Type.MOUSE.getOrCreate(button));
			if (bl) {
				keybinding.sendBindingKeys();
				this.selectedKey = null;
			}
			this.keyBindsList.refreshEntries();
			cir.setReturnValue(false);
		}
	}

	@Override
	public void patPat$onKeyReleased(int keyCode, int scanCode, int modifiers) {
		if (this.selectedKey instanceof PatPatKeybinding keybinding && keybinding.isBinding()) {
			keybinding.sendBindingKeys();
			this.selectedKey = null;
			this.keyBindsList.refreshEntries();
		}
	}
}
