package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.PatPatClientKeybindingManager;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	private void tickPatCooldown(CallbackInfo ci) {
		int tick = PatPatClientManager.getPatCooldown();
		if (--tick >= 0) {
			PatPatClientManager.setPatCooldown(tick);
		}
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", ordinal = 0, opcode = Opcodes.GETFIELD), method = "tick")
	private void continuePatsWhenPressed(CallbackInfo ci) {
		PatPatClientManager.requestPat();
	}

}
