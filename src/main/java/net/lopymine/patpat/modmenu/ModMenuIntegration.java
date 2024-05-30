package net.lopymine.patpat.modmenu;

import com.terraformersmc.modmenu.api.*;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.modmenu.screen.NoYACLScreen;
import net.lopymine.patpat.modmenu.screen.yacl.YACLConfigurationScreen;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
			return YACLConfigurationScreen::createScreen;
		} else {
			return NoYACLScreen::createScreen;
		}
	}
}
