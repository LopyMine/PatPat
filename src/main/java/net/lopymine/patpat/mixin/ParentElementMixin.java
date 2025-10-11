package net.lopymine.patpat.mixin;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;
/*? if >=1.21.9 {*/
/*import net.minecraft.client.input.KeyEvent;
*//*?}*/
@Mixin(ContainerEventHandler.class)
public interface ParentElementMixin {

	@Inject(at = @At("HEAD"), method = "keyReleased")
	private void handleScreenWithPatPatKeybindings(/*? if <1.21.9 {*/int keyCode, int scanCode, int modifiers/*?} else {*//*KeyEvent keyEvent*//*?}*/, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof ScreenWithPatPatKeybinding screen) {
			screen.patPat$onKeyReleased(/*? if <1.21.9 {*/keyCode, scanCode, modifiers/*?} else {*//*keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers()*//*?}*/);
		}
	}

}
