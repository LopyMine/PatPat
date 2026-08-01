package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.keybinding.*;
import net.lopymine.patpat.tests.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.objectweb.asm.Opcodes;
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
		PatPatTestClientAgent.onClientTick();
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", ordinal = 0, opcode = Opcodes.GETFIELD), method = "tick")
	private void continuePatsWhenPressed(CallbackInfo ci) {
		PatPatClientManager.requestPat();
	}

	@Inject(at = @At("HEAD"), method = "setScreen")
	private void clearPatPatKeybinding(Screen screen, CallbackInfo ci) {
		PatPatKeybinding patKeybinding = PatPatClientKeybindingManager.getPatKeybinding();
		if (patKeybinding == null) {
			return;
		}
		patKeybinding.refreshPressedState();
	}

	@Inject(at = @At("HEAD"), method = "close")
	private void testLogOnClosing(CallbackInfo ci) {
		if (!PatPatTestMode.isEnabled()) {
			return;
		}
		PatPatTestAgent.LOGGER.info("Closing the game...", new Exception());
	}

}
