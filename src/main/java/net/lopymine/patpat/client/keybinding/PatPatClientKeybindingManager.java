package net.lopymine.patpat.client.keybinding;

import lombok.Getter;
import net.minecraft.client.KeyMapping;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;

public class PatPatClientKeybindingManager {

	@Getter
	private static PatPatKeybinding patKeybinding;

	private PatPatClientKeybindingManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		if(patKeybinding != null){
			PatPatClient.LOGGER.error("PatPatClientKeybindingManager.register cannot called twice!");
			return;
		}
		patKeybinding = new PatPatKeybinding(PatPatClientConfig.getInstance().getMainConfig().getPatCombination());
		registerKeybinding(patKeybinding);
	}

	private static void registerKeybinding(KeyMapping keyBinding) {
		KeyBindingHelper.registerKeyBinding(keyBinding);
	}

}