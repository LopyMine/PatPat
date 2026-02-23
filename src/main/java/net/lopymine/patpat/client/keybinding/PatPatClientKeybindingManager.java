package net.lopymine.patpat.client.keybinding;

import lombok.Getter;
import com.mojang.blaze3d.platform.InputConstants;
import net.lopymine.patpat.entrypoint.MultiLoader;
import net.minecraft.client.gui.screens.controls.*;
import net.minecraft.client.KeyMapping;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;

//? if >=1.21.9 {
/*import net.lopymine.patpat.utils.RLUtils;
*///?}

//? if >=1.19.4 {
import net.lopymine.patpat.mixin.controlling.KeyBindsScreenAccessor;
//?}

public class PatPatClientKeybindingManager {

	//? if >=1.21.9 {
	/*public static final net.minecraft.client.KeyMapping.Category CATEGORY = net.minecraft.client.KeyMapping.Category.register(RLUtils.modId("keybinding"));
	*///?}

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
		MultiLoader.getInstance().registerKeybinding(keyBinding);
	}

	public static void handlePatPatKeybindingOnKeyPressed(
			KeyMapping mapping,
			/*? if >=1.18 {*/KeyBindsScreen/*?} else {*/ /*KeyBindsScreen *//*?}*/ screen,
			int keyCode,
			int scanCode,
			Runnable cancel
	) {
		if (mapping instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(getKey(keyCode, scanCode));
			if (bl) {
				keybinding.sendBindingKeys();
				screen.selectedKey = null;
			}
			//? if >=1.19.4 {
			((KeyBindsScreenAccessor) (screen)).getList().refreshEntries();
			//?}
			cancel.run();
		}
	}

	public static void handlePatPatKeybindingOnMouseClick(
			KeyMapping mapping,
			/*? if >=1.18 {*/KeyBindsScreen/*?} else {*/ /*KeyBindsScreen *//*?}*/ screen,
			int button,
			Runnable cancel
	) {
		if (mapping instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(InputConstants.Type.MOUSE.getOrCreate(button));
			if (bl) {
				keybinding.sendBindingKeys();
				screen.selectedKey = null;
			}
			//? if >=1.19.4 {
			((KeyBindsScreenAccessor) (screen)).getList().refreshEntries();
			//?}
			cancel.run();
		}
	}

	public static InputConstants.Key getKey(int keyCode, int scanCode) {
		return keyCode == -1 ? InputConstants.Type.SCANCODE.getOrCreate(scanCode) : InputConstants.Type.KEYSYM.getOrCreate(keyCode);
	}

}