package net.lopymine.patpat.mixin;

import net.lopymine.patpat.utils.mixin.ScreenWithPatPatKeybinding;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
public interface ParentElementMixin {

	@Inject(at = @At("HEAD"), method = "keyReleased")
	private void handleScreenWithPatPatKeybindings(net.minecraft.client.input.KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof ScreenWithPatPatKeybinding screen) {
			screen.patPat$onKeyReleased();
		}
	}

}
