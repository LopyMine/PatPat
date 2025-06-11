package net.lopymine.patpat.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.patpat.client.manager.PatPatClientManager;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	private void tickPatCooldown(CallbackInfo ci) {
		int tick = PatPatClientManager.getPatCooldown();
		if (--tick >= 0) {
			PatPatClientManager.setPatCooldown(tick);
		}
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", ordinal = 0), method = "tick")
	private void continuePatsWhenPressed(CallbackInfo ci) {
		PatPatClientManager.requestPat();
	}
}
