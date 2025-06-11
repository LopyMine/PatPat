package net.lopymine.patpat.client.keybinding;

import net.minecraft.client.KeyMapping;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.lopymine.patpat.client.config.PatPatClientConfig;

public class PatPatClientKeybindingManager {

	public static PatPatKeybinding PAT_KEYBINDING;

	public static void register() {
		PAT_KEYBINDING = new PatPatKeybinding(PatPatClientConfig.getInstance().getMainConfig().getPatKeys().keySet());
		registerKeybinding(PAT_KEYBINDING);
	}

	private static void registerKeybinding(KeyMapping keyBinding) {
		KeyBindingHelper.registerKeyBinding(keyBinding);
	}

}