package net.lopymine.patpat.client.keybinding;

import lombok.*;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.manager.PatPatClientManager;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class PatPatKeybinding extends KeyMapping {

	public static final KeybindingCombination DEFAULT_COMBINATION = new KeybindingCombination(
			InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_LEFT_SHIFT),
			InputConstants.Type.MOUSE.getOrCreate(GLFW.GLFW_MOUSE_BUTTON_2)
	);

	private final PressableKeybindingCombination combination = new PressableKeybindingCombination();
	@Getter
	private boolean binding;
	@Getter
	private boolean canStartBinding = true;

	public PatPatKeybinding(KeybindingCombination patCombination) {
		super("patpat.keybinding.pat", -1, PatPat.MOD_NAME);
		this.combination.setAttributeKey(patCombination.getAttributeKey());
		this.combination.setKey(patCombination.getKey());
	}

	public void startBinding() {
		this.resetPressedState();
		this.combination.setAttributeKey(null);
		this.combination.setKey(null);
		this.binding         = true;
		this.canStartBinding = false;
	}

	public boolean addBindingKey(InputConstants.Key key) {
		if (this.isCanStartBinding()) {
			this.startBinding();
		}
		if (!this.isBinding()) {
			return true;
		}
		if (key.getValue() == GLFW.GLFW_KEY_ESCAPE) {
			this.resetPressedState();
			this.combination.setKey(null);
			this.combination.setAttributeKey(null);
			return true;
		}
		if (KeybindingCombination.isAttributeKey(key.getValue())) {
			if (this.combination.isComplete()) {
				return true;
			}
			this.combination.setAttributeKey(key);
			return this.combination.isComplete();
		} else {
			this.combination.setKey(key);
			return true;
		}
	}

	public void sendBindingKeys() {
		this.saveCombination();
		this.resetPressedState();
		this.combination.setAll(false);
		this.binding         = false;
		this.canStartBinding = true;
	}

	@Override
	public void setKey(InputConstants.Key boundKey) {
		if (boundKey.equals(this.getDefaultKey())) {
			this.resetPressedState();
			this.combination.setAttributeKey(DEFAULT_COMBINATION.getAttributeKey());
			this.combination.setKey(DEFAULT_COMBINATION.getKey());
			this.saveCombination();
		}
	}

	private void saveCombination() {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		config.getMainConfig().setPatCombination(this.combination);
		config.saveAsync();
	}

	public boolean onKeyAction(InputConstants.Key key, boolean pressed) {
		if (!this.combination.contains(key)) {
			return false;
		}

		if (this.combination.onlyOneKey()) {
			this.combination.set(key, pressed);
			this.setDown(pressed);
			if(!pressed){
				PatPatClientManager.setPatCooldown(0);
			}
			return this.isDown();
		}

		if (!pressed) {
			this.combination.set(key, false);
			this.setDown(false);
			PatPatClientManager.setPatCooldown(0);
			return false;
		}

		this.combination.set(key, true);
		boolean allPressedState = this.combination.allPressed();
		this.setDown(allPressedState);

		return allPressedState && !KeybindingCombination.isAttributeKey(key.getValue());
	}

	@Override
	public boolean isDefault() {
		return this.combination.equals(DEFAULT_COMBINATION);
	}

	@Override
	@NotNull
	public Component getTranslatedKeyMessage() {
		//? if >=1.19 {
		return this.getFullTranslatedKeyMessage();
		//?} else {
		/*if (this.combination.onlyOneKey()) {
			return this.getFullTranslatedKeyMessage();
		} else {
			return TextUtils.literal("...");
		}
		*///?}
	}

	@NotNull
	public Component getFullTranslatedKeyMessage() {
		return this.combination.getCombinationLocalizedComponent(!this.isBinding());
	}

	public void resetPressedState() {
		List<Key> keys = this.combination.getKeys();
		keys.forEach(key -> {
			if (key.getType() == Type.KEYSYM) {
				this.combination.set(key, InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue()));
			} else {
				this.combination.set(key, GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), key.getValue()) == 1);
			}
		});
		boolean allPressed = this.combination.allPressed();
		this.setDown(allPressed);
	}
}
