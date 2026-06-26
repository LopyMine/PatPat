package net.lopymine.patpat.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.lopymine.patpat.mixin.KeyBindsScreenAccessor;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;

public class PatPatClientKeybindingManager {

	public static final net.minecraft.client.KeyMapping.Category CATEGORY = net.minecraft.client.KeyMapping.Category.register(RLUtils.modId("keybinding"));

	@Getter
	private static PatPatKeybinding patKeybinding;

	private PatPatClientKeybindingManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		if (patKeybinding != null) {
			PatPatClient.LOGGER.error("PatPatClientKeybindingManager.register cannot called twice!");
			return;
		}
		patKeybinding = new PatPatKeybinding(PatPatClientConfig.getInstance().getMainConfig().getPatCombination());
		registerKeybinding(patKeybinding);
	}

	private static void registerKeybinding(KeyMapping keyBinding) {
		ClientMultiLoader.getInstance().registerKeybinding(keyBinding);
	}

	public static void handlePatPatKeybindingOnKeyPressed(
			KeyMapping mapping,
			KeyBindsScreen screen,
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
			((KeyBindsScreenAccessor) (screen)).getList().refreshEntries();
			cancel.run();
		}
	}

	public static void handlePatPatKeybindingOnMouseClick(
			KeyMapping mapping,
			KeyBindsScreen screen,
			int button,
			Runnable cancel
	) {
		if (mapping instanceof PatPatKeybinding keybinding) {
			boolean bl = keybinding.addBindingKey(InputConstants.Type.MOUSE.getOrCreate(button));
			if (bl) {
				keybinding.sendBindingKeys();
				screen.selectedKey = null;
			}
			((KeyBindsScreenAccessor) (screen)).getList().refreshEntries();
			cancel.run();
		}
	}

	public static InputConstants.Key getKey(int keyCode, int scanCode) {
		return keyCode == -1 ? InputConstants.Type.SCANCODE.getOrCreate(scanCode) : InputConstants.Type.KEYSYM.getOrCreate(keyCode);
	}

}