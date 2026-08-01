package net.lopymine.patpat.mixin.controlling;

//? if >=1.17.1 && <1.19.3 && controlling {
/*import com.blamejared.controlling.client.NewKeyBindsList;
import com.blamejared.controlling.client.NewKeyBindsList.KeyEntry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.utils.mixin.IRequestableTooltipScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NewKeyBindsList.class)
public class NewKeyBindsListMixin {

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/controls/KeyBindsScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;II)V"), method = "renderDecorations")
	private boolean cancelUselessTooltip(KeyBindsScreen instance, PoseStack stack, Component component, int x, int y, @Local KeyEntry entry) {
		if (instance instanceof IRequestableTooltipScreen screen) {
			return screen.myTotemDoll$getCurrentRequest() == null;
		}
		return true;
	}

}
*///?}
