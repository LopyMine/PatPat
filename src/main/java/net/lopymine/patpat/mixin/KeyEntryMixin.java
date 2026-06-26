package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.PatPatKeybinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList.KeyEntry;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyEntry.class)
public class KeyEntryMixin {

	@Shadow
	@Final
	private KeyMapping key;

	@Shadow
	@Final
	private Button changeButton;


	@Inject(at = @At("TAIL"), method = "refreshEntry")
	private void addPatPatTooltipToPatPatKey(CallbackInfo ci) {
		if (!(this.key instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}
		this.changeButton.setTooltip(Tooltip.create(patPatKeybinding.getTranslatedKeyMessage()));
	}

}
