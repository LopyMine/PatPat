package net.lopymine.patpat.mixin.tests;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens./*? if >=1.21 {*/options./*?}*/controls./*? if >=1.18 {*/ KeyBindsList /*?} else {*//*ControlList*//*?}*/.KeyEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyEntry.class)
public interface KeyEntryAccessor {

	@Accessor("key")
	KeyMapping getKeyMapping();

	@Accessor("changeButton")
	Button getChangeButton();

}
