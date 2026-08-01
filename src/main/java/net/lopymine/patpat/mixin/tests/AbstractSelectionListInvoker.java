package net.lopymine.patpat.mixin.tests;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSelectionList.class)
public interface AbstractSelectionListInvoker {

	@Invoker("getRowTop")
	int invokeGetRowTop(int index);

}
