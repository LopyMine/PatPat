package net.lopymine.patpat.client.keybinding;

import lombok.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.*;

import com.mojang.blaze3d.platform.InputConstants;

import net.lopymine.patpat.client.manager.PatPatClientManager;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class PatPatKeybinding extends KeyMapping {

	private static final Combination defaultCombination = new Combination(InputConstants.Type.KEYSYM.getOrCreate(340), InputConstants.Type.MOUSE.getOrCreate(1));

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

	private final Combination bindingKeys = new Combination();
	@Getter
	private final Map<InputConstants.Key, Boolean> keys;
	@Getter
	private boolean binding;
	@Getter
	private boolean canStartBinding = true;

	public PatPatKeybinding(Set<InputConstants.Key> keys) {
		super("patpat.keybinding.pat", InputConstants.UNKNOWN.getValue(), "PatPat");
		this.keys = keys.stream()
				.map(key -> Map.entry(key, false))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	public boolean addBindingKey(InputConstants.Key key) {
		System.out.println("ADDING KEYBIND");
		if (ATTRIBUTE_KEYS.contains(key.getName())) {
			if (this.bindingKeys.isComplete()) {
				return true;
			}
			this.binding         = true;
			this.canStartBinding = false;
			this.bindingKeys.setAttributeKey(key);
			System.out.println("ADDED KEYBIND");
			return this.bindingKeys.isComplete();
		} else {
			this.bindingKeys.setKey(key);
			return true;
		}
	}

	public void sendBindingKeys() {
		System.out.println("SENDING KEYBIND");
		this.keys.clear();
		Stream.of(this.bindingKeys.attributeKey, this.bindingKeys.key).filter(Objects::nonNull).forEach(key -> this.keys.put(key, false));
		this.bindingKeys.clear();
		this.binding         = false;
		this.canStartBinding = true;
		System.out.println("SENDED KEYBIND");
	}

	public boolean containsKey(InputConstants.Key key) {
		return this.keys.containsKey(key);
	}

	@Override
	public void setKey(InputConstants.Key boundKey) {
		if (boundKey.equals(this.getDefaultKey())) {
			this.keys.clear();
			Stream.of(defaultCombination.attributeKey, defaultCombination.key).filter(Objects::nonNull).forEach(key -> this.keys.put(key, false));
			this.bindingKeys.clear();
			System.out.println("CLEAR KEYBIND");
		}
	}


	public boolean onKeyAction(InputConstants.Key key, boolean pressed) {
		if (!this.keys.containsKey(key)) return false;

		System.out.printf("%s %s %s", key, pressed, this.isDown());

		if (keys.size() == 1) {
			keys.put(key, pressed);
			this.setDown(pressed);
			System.out.printf(" -> %s(1)%n", this.isDown());
			return this.isDown();
		}

		if (!pressed) {
			keys.put(key, false);
			this.setDown(false);
			PatPatClientManager.setPatCooldown(0);
			System.out.printf(" -> %s(2)%n", this.isDown());
			return false;
		}

		boolean isAttribute = ATTRIBUTE_KEYS.contains(key.getName());
		boolean allPressed = keys.values().stream().allMatch(v -> v);
		boolean anyPressed = keys.values().stream().anyMatch(v -> v);

		if (isAttribute && !allPressed && anyPressed) {
			this.setDown(false);
			System.out.printf(" -> %s(3)%n", this.isDown());
			return false;
		}

		keys.put(key, true);
		boolean newState = keys.values().stream().allMatch(v -> v);
		this.setDown(newState);
		System.out.printf(" -> %s(4)%n", this.isDown());
		return newState;
	}

	@Override
	public boolean isDefault() {
		return this.bindingKeys.equals(defaultCombination);
	}

	@Override
	public @NotNull Component getTranslatedKeyMessage() {
		MutableComponent result = Component.literal("");
		if (!this.bindingKeys.isEmpty()) {
			return this.bindingKeys.getCombinationLocalizedComponent();
		}
		Set<InputConstants.Key> keys = this.keys.keySet();
		if (keys.isEmpty()) {
			return InputConstants.UNKNOWN.getDisplayName();
		}
		System.out.println(keys);
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

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Combination {

		private static final Component COLLECT_TEXT = Component.literal(" + ");

		private InputConstants.Key attributeKey;
		private InputConstants.Key key;

		public void clear() {
			this.attributeKey = null;
			this.key          = null;
		}

		public boolean isComplete() {
			return this.attributeKey != null && this.key != null;
		}

		public boolean isEmpty() {
			return this.attributeKey == null && this.key == null;
		}

		public Component getCombinationLocalizedComponent() {
			if (this.attributeKey != null) {
				MutableComponent component = this.attributeKey.getDisplayName().copy().append(COLLECT_TEXT);
				if (this.key != null) {
					component.append(this.key.getDisplayName());
				}
				return component;
			}
			if (this.key != null) {
				return this.key.getDisplayName();
			}
			return InputConstants.UNKNOWN.getDisplayName();
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			Combination that = (Combination) o;
			return Objects.equals(this.attributeKey, that.attributeKey) && Objects.equals(this.key, that.key);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.attributeKey, this.key);
		}
	}
}
