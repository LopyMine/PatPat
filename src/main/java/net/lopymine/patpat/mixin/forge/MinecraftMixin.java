package net.lopymine.patpat.mixin.forge;

//? if forge {

/*import net.lopymine.patpat.entrypoint.forge.event.PatPatForgeClientStoppingEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V",
					ordinal = 0,
					remap = false
			),
			method = "destroy"
	)
	private void onStopping(CallbackInfo ci) {
		MinecraftForge.EVENT_BUS.post(new PatPatForgeClientStoppingEvent());
	}

}
*///?}
