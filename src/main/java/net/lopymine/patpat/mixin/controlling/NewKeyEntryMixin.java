package net.lopymine.patpat.mixin.controlling;

//? if >=1.17.1 && controlling {

import com.blamejared.controlling.client.NewKeyBindsList;
import com.blamejared.controlling.client.NewKeyBindsList.KeyEntry;
import net.lopymine.patpat.client.keybinding.PatPatKeybinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.options.controls.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyEntry.class)
public class NewKeyEntryMixin {

	//? if >=1.19.4 {
	@Shadow/*? if forge {*//*(remap = false)*//*?}*/
	@Final
	private KeyMapping key;
	//?} else {
	/*@Shadow
	@Final
	private KeyMapping keybinding;
	*///?}

	@Shadow/*? if forge {*//*(remap = false)*//*?}*/
	@Final
	private Button btnChangeKeyBinding;

	//? if <1.19.3 {
	/*@Inject(at = @At("TAIL"), method = "<init>")
	private void init(NewKeyBindsList list, KeyMapping name, CallbackInfo ci) {
		if (!(this.keybinding instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}

		this.btnChangeKeyBinding.onTooltip = (button, poseStack, a, b) -> {
			/^? if >=1.18 {^/
			KeyBindsScreen screen = list.keyBindsScreen;
			 /^?} else {^/
			/^KeyBindsScreen screen = ((NewKeyBindsListAccessor) list).getScreenPleaseThanks();
			^//^?}^/
			((net.lopymine.patpat.utils.mixin.IRequestableTooltipScreen) screen).myTotemDoll$requestTooltip((pose, x, y, d) -> {
				screen.renderTooltip(poseStack, patPatKeybinding.getFullTranslatedKeyMessage(), x, y);
			});
		};
	}
	*///?}

	@Inject(at = @At(/*? if >=1.19.4 {*/ "TAIL" /*?} else {*/ /*"HEAD" *//*?}*/), method = /*? if >=1.19.4 {*/ "refreshEntry" /*?} else {*/ /*"render" *//*?}*/)
	private void addPatPatTooltipToPatPatKey(CallbackInfo ci) {
		//? if >=1.19.4 {
		if (!(this.key instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}
		this.btnChangeKeyBinding.setTooltip(Tooltip.create(patPatKeybinding.getTranslatedKeyMessage()));
		//?}
	}

}
//?}
