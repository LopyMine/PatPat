package net.lopymine.patpat.mixin.tests;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyboardHandler.class)
public interface KeyboardHandlerInvoker {

	@Invoker("keyPress")
	void invokeKeyPress(
			//? if >=1.21.9 {
			long window, int action, net.minecraft.client.input.KeyEvent event
			//?} else {
			/*long window, int key, int scancode, int action, int modifiers
			*///?}
	);

}
