package net.lopymine.patpat.mixin;

//? if <1.19.3 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.utils.mixin.*;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin implements IRequestableTooltipScreen {

	@Unique
	private TooltipRequest tooltipRequest;

	@Inject(at = @At("TAIL"), method = "render")
	private void renderWithTooltip(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (this.tooltipRequest != null) {
			this.tooltipRequest.render(poseStack, mouseX, mouseY, partialTick);
			this.tooltipRequest = null;
		}
	}

	@Override
	public void myTotemDoll$requestTooltip(TooltipRequest tooltipRequest) {
		this.tooltipRequest = tooltipRequest;
	}

	@Override
	public TooltipRequest myTotemDoll$getCurrentRequest() {
		return this.tooltipRequest;
	}
}
*///?}
