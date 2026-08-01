package net.lopymine.patpat.mixin.tests;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MouseHandler.class)
public interface MouseHandlerInvoker {

	@Invoker("onMove")
	void invokeOnMove(long window, double x, double y);

	@Invoker("onScroll")
	void invokeOnScroll(long window, double x, double y);

	//? if >=1.21.9 {
	@Invoker("onButton")
	void invokeOnButton(long window, net.minecraft.client.input.MouseButtonInfo info, int action);
	//?} else {
	/*@Invoker("onPress")
	void invokeOnPress(long window, int button, int action, int modifiers);
	*///?}

}
