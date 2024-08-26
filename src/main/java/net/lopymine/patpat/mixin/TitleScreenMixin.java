package net.lopymine.patpat.mixin;

//? =1.16.5 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isMultiplayerEnabled()Z"), method = "initWidgetsNormal")
	private boolean init(MinecraftClient instance, Operation<Boolean> original) {
		return FabricLoader.getInstance().isDevelopmentEnvironment() || original.call(instance);
	}
}
*///?}
