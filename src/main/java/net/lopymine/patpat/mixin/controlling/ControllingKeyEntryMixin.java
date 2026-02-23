package net.lopymine.patpat.mixin.controlling;

//? if =1.16.5 && controlling {

/*import com.blamejared.controlling.client.gui.KeyBindingListWidgetNew;
import com.blamejared.controlling.client.gui.KeyBindingListWidgetNew.KeyEntry;
import net.lopymine.patpat.client.keybinding.PatPatKeybinding;
import net.lopymine.patpat.utils.mixin.IRequestableTooltipScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.controls.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyEntry.class)
public class ControllingKeyEntryMixin {

	@Shadow @Final public KeyMapping binding;

	@Shadow @Final public Button editButton;

	@Inject(at = @At("TAIL"), method = "<init>(Lcom/blamejared/controlling/client/gui/KeyBindingListWidgetNew;Lnet/minecraft/client/KeyMapping;)V")
	private void init(KeyBindingListWidgetNew list, KeyMapping name, CallbackInfo ci) {
		if (!(this.binding instanceof PatPatKeybinding patPatKeybinding)) {
			return;
		}

		this.editButton.onTooltip = (button, poseStack, a, b) -> {
			KeyBindsScreen screen = ((ControllingKeyBindingListWidgetNewAccessor) list).getScreenPleaseThanks();
			((IRequestableTooltipScreen) screen).myTotemDoll$requestTooltip((pose, x, y, d) -> {
				screen.renderTooltip(poseStack, patPatKeybinding.getFullTranslatedKeyMessage(), x, y);
			});
		};
	}

}
*///?}
