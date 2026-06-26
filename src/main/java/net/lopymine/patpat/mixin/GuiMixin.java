package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

	@Inject(at = @At("HEAD"), method = "setScreen")
	private void clearPatPatKeybinding(Screen screen, CallbackInfo ci) {
		PatPatClientKeybindingManager.getPatKeybinding().refreshPressedState();
	}

}
