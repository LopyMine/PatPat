package net.lopymine.patpat.mixin.controlling;

//? if controlling {

import com.blamejared.controlling.client.NewKeyBindsList.KeyEntry;
import net.lopymine.patpat.client.keybinding.PatPatKeybinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyEntry.class)
public class NewKeyEntryMixin {

	@Shadow
	@Final
	private KeyMapping key;

	@Shadow
	@Final
	private Button btnChangeKeyBinding;


	@Inject(at = @At("TAIL"), method = "refreshEntry")
	private void addPatPatTooltipToPatPatKey(CallbackInfo ci) {
		if (!(this.key instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}
		this.btnChangeKeyBinding.setTooltip(Tooltip.create(patPatKeybinding.getTranslatedKeyMessage()));
	}

}
//?}
