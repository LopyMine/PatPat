package net.lopymine.patpat.mixin;

//? if >=1.19.4 {

import net.minecraft.client.gui.screens.options.controls.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindsScreen.class)
public interface KeyBindsScreenAccessor {

	@Accessor("keyBindsList")
	KeyBindsList getList();

}
//?}
