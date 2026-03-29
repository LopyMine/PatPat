package net.lopymine.patpat.mixin.controlling;

//? if =1.16.5 && controlling {

/*import com.blamejared.controlling.client.gui.ControllingOptionsScreen;
import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ControllingOptionsScreen.class)
public class ControlsSettingsGuiNewMixin extends KeyBindsScreen {

	public ControlsSettingsGuiNewMixin(Screen screen, Options options) {
		super(screen, options);
	}

	@Inject(
			at = @At(
					value = "FIELD",
					target = "Lcom/blamejared/controlling/client/gui/ControllingOptionsScreen;selectedKey:Lnet/minecraft/client/KeyMapping;",
					ordinal = 1,
					shift = Shift.AFTER
			),
			method = "keyPressed",
			cancellable = true
	)
	private void handlePatPatKeybindingOnKeyPressed(int keyCode, int scanCode, int mods, CallbackInfoReturnable<Boolean> cir) {
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(this.selectedKey, this, keyCode, scanCode, () -> cir.setReturnValue(false));
	}

}
*///?}
