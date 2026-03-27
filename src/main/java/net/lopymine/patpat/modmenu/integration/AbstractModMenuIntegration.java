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


//?} elif neoforge {

/*import net.neoforged.fml.*;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parent) -> createConfigScreen(parent));
	}

	protected abstract Screen createConfigScreen(Screen parent);

}

*///?} elif forge {

/*import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.*;

public abstract class AbstractModMenuIntegration {

	public void register(ModContainer container) {
		container.registerExtensionPoint(
				ConfigScreenFactory.class,
				() -> new ConfigScreenFactory((minecraft, parent) -> this.createConfigScreen(parent))
		);
	}

	protected abstract Screen createConfigScreen(Screen parent);

}

*///?}
