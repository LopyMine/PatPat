package net.lopymine.patpat.modmenu.integration;

import net.lopymine.patpat.entrypoint.*;
import net.lopymine.patpat.modmenu.screen.NoConfigLibrariesScreen;
import net.lopymine.patpat.modmenu.bridge.*;
import net.lopymine.patpat.utils.VersionedThings;
import net.minecraft.client.gui.screens.Screen;

public class PatPatModMenuIntegration extends AbstractModMenuIntegration {

	@Override
	protected Screen createConfigScreen(Screen parent) {
		//? >=1.20.1 && yacl {
		if (EarlyCommonMultiLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
			return YACLBridge.getScreen(parent);
		}
		//?}
		//? if cloth-config {
		if (EarlyCommonMultiLoader.getInstance().isModLoaded(VersionedThings.CLOTH_CONFIG_ID)) {
			return ClothConfigBridge.getScreen(parent);
		}

		//?}
		return NoConfigLibrariesScreen.createScreen(parent);
	}
}
