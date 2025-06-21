package net.lopymine.patpat.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens./*? if >=1.21 {*//*options.*//*?}*/controls.KeyBindsList.KeyEntry;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <1.19.3 {
import net.minecraft.client.gui.screens.controls.*;
import net.minecraft.network.chat.Component;
//?}

import net.lopymine.patpat.client.keybinding.PatPatKeybinding;

@Mixin(KeyEntry.class)
public class ControlsListWidgetMixin {

	@Shadow
	@Final
	private KeyMapping key;

	@Shadow
	@Final
	private Button changeButton;

	//? if <1.19.3 {
	/*@Inject(at = @At("TAIL"), method = "<init>")
	private void init(KeyBindsList keyBindsList, KeyMapping keyMapping, Component component, CallbackInfo ci) {
		if (!(this.key instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}

		this.changeButton.onTooltip = (button, poseStack, a, b) -> {
			if (patPatKeybinding.isDefault()) {
				return;
			}
			keyBindsList.keyBindsScreen.renderTooltip(poseStack, patPatKeybinding.getTranslatedKeyMessage(), a, b);
		};
	}
	*///?}


	@Inject(at = @At(/*? if >=1.19.4 {*/ "TAIL" /*?} else {*/ /*"HEAD" *//*?}*/), method = /*? if >=1.19.4 {*/ "refreshEntry" /*?} else {*/ /*"render" *//*?}*/)
	private void addPatPatTooltipToPatPatKey(CallbackInfo ci) {
		//? if >=1.19.4 {
		if (!(this.key instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}
		if (patPatKeybinding.isDefault()) {
			this.changeButton.setTooltip(null);
		} else {
			this.changeButton.setTooltip(Tooltip.create(patPatKeybinding.getTranslatedKeyMessage()));
		}
		//?}
	}

}
