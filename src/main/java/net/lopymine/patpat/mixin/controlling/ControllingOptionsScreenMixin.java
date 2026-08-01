package net.lopymine.patpat.mixin.controlling;

//? if <=1.17 && controlling  {

/*import com.blamejared.controlling.client.gui.ControllingOptionsScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.utils.mixin.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControllingOptionsScreen.class)
public class ControllingOptionsScreenMixin {

	@Inject(at = @At("TAIL"), method = "render")
	private void renderWithTooltip(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (this instanceof IRequestableTooltipScreen screen) {
			TooltipRequest tooltipRequest = screen.myTotemDoll$getCurrentRequest();
			if (tooltipRequest != null) {
				tooltipRequest.render(poseStack, mouseX, mouseY, partialTick);
				screen.myTotemDoll$requestTooltip(null);
			}
		}
	}

}
*///?}
