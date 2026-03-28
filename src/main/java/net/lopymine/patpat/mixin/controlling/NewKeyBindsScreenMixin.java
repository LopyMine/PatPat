package net.lopymine.patpat.mixin.controlling;

//? if >=1.17.1 && <=1.20.6 && controlling {

/*import com.blamejared.controlling.client.NewKeyBindsScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.utils.mixin.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.*;

@Debug(export = true)
@Mixin(NewKeyBindsScreen.class)
// Ignore the "Cannot find 'KeyBindsScreen' in the hierarchy of target class 'NewKeyBindsScreen'"
// That's fine
public abstract class NewKeyBindsScreenMixin extends KeyBindsScreen {

	public NewKeyBindsScreenMixin(Screen screen, Options options) {
		super(screen, options);
	}

	@Inject(
			at = @At(
					value = "FIELD",
					target = "Lcom/blamejared/controlling/client/NewKeyBindsScreen;selectedKey:Lnet/minecraft/client/KeyMapping;",
					ordinal = 1,
					shift = Shift.AFTER,
					opcode = Opcodes.GETFIELD
			),
			method = "keyPressed",
			cancellable = true
	)
	private void handlePatPatKeybindingOnKeyPressed(int keyCode, int scanCode, int mods, CallbackInfoReturnable<Boolean> cir) {
		PatPatClientKeybindingManager.handlePatPatKeybindingOnKeyPressed(this.selectedKey, this, keyCode, scanCode, () -> cir.setReturnValue(false));
	}

	//? if <1.19.3 {
	/^@Inject(at = @At("TAIL"), method = "render")
	private void renderWithTooltip(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (this instanceof IRequestableTooltipScreen screen) {
			TooltipRequest tooltipRequest = screen.myTotemDoll$getCurrentRequest();
			if (tooltipRequest != null) {
				tooltipRequest.render(poseStack, mouseX, mouseY, partialTick);
				screen.myTotemDoll$requestTooltip(null);
			}
		}
	}
	^///?}

}
*///?}
