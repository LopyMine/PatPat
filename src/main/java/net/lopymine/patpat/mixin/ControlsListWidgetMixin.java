package net.lopymine.patpat.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList.KeyEntry;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.client.keybinding.PatPatKeybinding;

@Mixin(KeyEntry.class)
public class ControlsListWidgetMixin {

	@Shadow @Final private KeyMapping key;

	@Shadow @Final private Button changeButton;

	@Inject(at = @At("TAIL"), method = "refreshEntry")
	private void addPatPatTooltipToPatPatKey(CallbackInfo ci) {
		if (!(this.key instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}

		if (patPatKeybinding.isDefault()) {
			this.changeButton.setTooltip(null);
		} else {
			this.changeButton.setTooltip(Tooltip.create(patPatKeybinding.getTranslatedKeyMessage()));
		}
	}

}
