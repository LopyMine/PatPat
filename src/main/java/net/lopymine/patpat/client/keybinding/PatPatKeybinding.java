package net.lopymine.patpat.client.keybinding;

import lombok.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.*;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.manager.PatPatClientManager;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class PatPatKeybinding extends KeyMapping {

	public static final KeybindingCombination DEFAULT_COMBINATION = new KeybindingCombination(
			InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_LEFT_SHIFT),
			InputConstants.Type.MOUSE.getOrCreate(GLFW.GLFW_MOUSE_BUTTON_2)
	);

	private static final List<String> ATTRIBUTE_KEYS = List.of(
			"key.keyboard.left.alt",
			"key.keyboard.left.control",
			"key.keyboard.left.shift",
			"key.keyboard.left.win",
			"key.keyboard.right.alt",
			"key.keyboard.right.control",
			"key.keyboard.right.shift",
			"key.keyboard.right.win",
			"key.keyboard.tab",
			"key.keyboard.caps.lock"
	);

	private final KeybindingCombination combination = new KeybindingCombination();
	@Getter
	private final Map<InputConstants.Key, Boolean> keys;
	@Getter
	private boolean binding;
	@Getter
	private boolean canStartBinding = true;

	public PatPatKeybinding(KeybindingCombination patCombination) {
		super("patpat.keybinding.pat", InputConstants.UNKNOWN.getValue(), PatPat.MOD_NAME);

		Map<InputConstants.Key, Boolean> keys = new HashMap<>();

		if (!patCombination.getAttributeKey().equals(patCombination.getKey())) {
			keys = Stream.of(patCombination.getAttributeKey(), patCombination.getKey())
				.filter(key -> key.getValue() != -1)
				.map(key -> Map.entry(key, false))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}

		this.keys = keys;
	}

	public boolean addBindingKey(InputConstants.Key key) {
		if (ATTRIBUTE_KEYS.contains(key.getName())) {
			if (this.combination.isComplete()) {
				return true;
			}
			this.binding         = true;
			this.canStartBinding = false;
			this.combination.setAttributeKey(key);
			return this.combination.isComplete();
		} else {
			this.combination.setKey(key);
			return true;
		}
	}

	public void sendBindingKeys() {
		this.keys.clear();
		Stream.of(this.combination.getAttributeKey(), this.combination.getKey()).filter(Objects::nonNull).forEach(key -> this.keys.put(key, false));
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		config.getMainConfig().setPatCombination(this.combination);
		config.saveAsync();
		this.binding         = false;
		this.canStartBinding = true;
	}

	@Override
	public void setKey(InputConstants.Key boundKey) {
		if (boundKey.equals(this.getDefaultKey())) {
			this.keys.clear();
			Stream.of(DEFAULT_COMBINATION.getAttributeKey(), DEFAULT_COMBINATION.getKey()).filter(Objects::nonNull).forEach(key -> this.keys.put(key, false));
		}
	}

	public boolean onKeyAction(InputConstants.Key key, boolean pressed) {
		if (!this.keys.containsKey(key)) {
			return false;
		}

		if (keys.size() == 1) {
			keys.put(key, pressed);
			this.setDown(pressed);
			return this.isDown();
		}

		if (!pressed) {
			keys.put(key, false);
			this.setDown(false);
			PatPatClientManager.setPatCooldown(0);
			return false;
		}

		boolean isAttribute = ATTRIBUTE_KEYS.contains(key.getName());
		boolean allPressed = keys.values().stream().allMatch(v -> v);
		boolean anyPressed = keys.values().stream().anyMatch(v -> v);

		if (isAttribute && !allPressed && anyPressed) {
			this.setDown(false);
			return false;
		}

		keys.put(key, true);
		boolean newState = keys.values().stream().allMatch(v -> v);
		this.setDown(newState);
		return newState;
	}

	@Override
	public boolean isDefault() {
		return this.combination.equals(DEFAULT_COMBINATION);
	}

	@Override
	public @NotNull Component getTranslatedKeyMessage() {
		MutableComponent result = Component.literal("");
		if (this.isBinding()) {
			return this.combination.getCombinationLocalizedComponent();
		}
		Set<InputConstants.Key> keys = this.keys.keySet();
		if (keys.isEmpty()) {
			return InputConstants.UNKNOWN.getDisplayName();
		}

		List<Component> list = keys.stream().sorted((o1, o2) -> {
					if (o1.equals(o2)) {
						return 0;
					}
					return ATTRIBUTE_KEYS.contains(o1.getName()) ? -1 : 1;
				})
				.map(InputConstants.Key::getDisplayName)
				.toList();
		for (int i = 0; i < list.size(); i++) {
			Component part = list.get(i);
			result.append(part);
			if (i != list.size() - 1) {
				result.append(" + ");
			}
		}
		return result;
	}

	public void unPress() {
		this.setDown(false);
		Set<Key> set = this.keys.keySet();
		set.forEach((key) -> this.keys.put(key, false));
	}
}
