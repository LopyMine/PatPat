package net.lopymine.patpat.mixin;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;

@Mixin(ContainerEventHandler.class)
public interface ParentElementMixin {

	//? if >=1.21.9 {
	@Inject(at = @At("HEAD"), method = "keyReleased")
	private void handleScreenWithPatPatKeybindings(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof ScreenWithPatPatKeybinding screen) {
			screen.patPat$onKeyReleased(keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers());
		}
	}
	//?} else {
	/*@Inject(at = @At("HEAD"), method = "keyReleased")
	private void handleScreenWithPatPatKeybindings(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof ScreenWithPatPatKeybinding screen) {
			screen.patPat$onKeyReleased(keyCode, scanCode, modifiers);
		}
	}
	*///?}

}
