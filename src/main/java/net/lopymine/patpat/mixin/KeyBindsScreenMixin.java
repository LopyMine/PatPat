package net.lopymine.patpat.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens./*? if >=1.21 {*/options./*?}*/controls.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.InputConstants;

import net.lopymine.patpat.client.keybinding.PatPatKeybinding;
import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;

import org.jetbrains.annotations.Nullable;

@Mixin(/*? if >=1.18 {*/KeyBindsScreen/*?} else {*/ /*ControlsScreen *//*?}*/.class)
public class KeyBindsScreenMixin implements ScreenWithPatPatKeybinding {

	@Shadow @Nullable public KeyMapping selectedKey;

	//? if >=1.19.4 {
	@Shadow
	private KeyBindsList keyBindsList;
	//?}

	@Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
	private void handlePatPatKeybindingOnKeyPressed(/*? if >=1.21.9 {*/net.minecraft.client.input.KeyEvent event, /*?} else {*/ /*int keyCode, int scanCode, int modifiers, *//*?}*/ CallbackInfoReturnable<Boolean> cir) {
		if (this.selectedKey instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(InputConstants.getKey(/*? if >=1.21.9 {*/event/*?} else {*/  /*keyCode, scanCode  *//*?}*/));
			if (bl) {
				keybinding.sendBindingKeys();
				this.selectedKey = null;
			}
			//? if >=1.19.4 {
			this.keyBindsList.refreshEntries();
			//?}
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
	private void handlePatPatKeybindingOnMouseClick(/*? if >=1.21.9 {*/net.minecraft.client.input.MouseButtonEvent event, boolean b, /*?} else {*/ /*double mouseX, double mouseY, int button, *//*?}*/ CallbackInfoReturnable<Boolean> cir) {
		if (this.selectedKey instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(InputConstants.Type.MOUSE.getOrCreate(/*? if >=1.21.9 {*/ event.button() /*?} else {*/ /*button *//*?}*/));
			if (bl) {
				keybinding.sendBindingKeys();
				this.selectedKey = null;
			}
			//? if >=1.19.4 {
			this.keyBindsList.refreshEntries();
			//?}
			cir.setReturnValue(false);
		}
	}

	@Override
	public void patPat$onKeyReleased(int keyCode, int scanCode, int modifiers) {
		if (this.selectedKey instanceof PatPatKeybinding keybinding && keybinding.isBinding()) {
			keybinding.sendBindingKeys();
			this.selectedKey = null;
			//? if >=1.19.4 {
			this.keyBindsList.refreshEntries();
			//?}
		}
	}
}
