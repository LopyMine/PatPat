package net.lopymine.patpat.modmenu.integration;

//? if fabric {

import com.terraformersmc.modmenu.api.*;
import net.minecraft.client.gui.screens.Screen;

public abstract class AbstractModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::createConfigScreen;
	}

	protected abstract Screen createConfigScreen(Screen parent);
}


//?} elif neoforge && >=1.20.5 {

/*import net.neoforged.fml.*;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> createConfigScreen(parent));
	}

	protected abstract Screen createConfigScreen(Screen parent);

}

*///?} elif neoforge {

/*import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.neoforged.fml.*;

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(
				ConfigScreenFactory.class,
				() -> new ConfigScreenFactory((minecraft, parent) -> this.createConfigScreen(parent))
		);
	}

	protected abstract Screen createConfigScreen(Screen parent);

}

*///?} elif forge {

/*import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.*;

//? if >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
//?} elif >=1.18 {
/^import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
^///?} else {
/^import net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory;
^///?}

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		//? if >=1.19 {
		container.registerExtensionPoint(
				ConfigScreenFactory.class,
				() -> new ConfigScreenFactory((minecraft, parent) -> this.createConfigScreen(parent))
		);
		//?} else {
		/^container.registerExtensionPoint(
				ConfigGuiFactory.class,
				() -> new ConfigGuiFactory((minecraft, parent) -> this.createConfigScreen(parent))
		);
		^///?}
	}

	protected abstract Screen createConfigScreen(Screen parent);

}

*///?}
