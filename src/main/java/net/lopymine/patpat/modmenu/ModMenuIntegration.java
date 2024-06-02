package net.lopymine.patpat.modmenu;

import com.terraformersmc.modmenu.api.*;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.modmenu.screen.NoConfigLibrariesScreen;
import net.lopymine.patpat.modmenu.screen.clothconfig.ClothConfigConfigurationScreen;
import net.lopymine.patpat.modmenu.screen.yacl.YACLConfigurationScreen;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
			return YACLConfigurationScreen::createScreen;
		}
		if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
			return ClothConfigConfigurationScreen::createScreen;
		}
		return NoConfigLibrariesScreen::createScreen;
	}
}
