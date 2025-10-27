package net.lopymine.patpat.modmenu;

import com.terraformersmc.modmenu.api.*;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.modmenu.bridge.*;
import net.lopymine.patpat.utils.VersionedThings;

public class ModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		//? >=1.20.1 {
		if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
			return YACLBridge::getScreen;
		}
		//?}
		if (FabricLoader.getInstance().isModLoaded(VersionedThings.CLOTH_CONFIG_ID)) {
			return ClothConfigBridge::getScreen;
		}
		return NoConfigLibrariesScreen::createScreen;
	}
}
