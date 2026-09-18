package net.lopymine.patpat.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import java.util.stream.Stream;
import lombok.*;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeybindingCombination {

	public static final Codec<Key> KEY_CODEC = Codec.STRING.xmap(KeybindingCombination::getKeyByName, Key::getName);
	public static final Codec<KeybindingCombination> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			KEY_CODEC.optionalFieldOf("attributeKey").xmap((o) -> o.orElse(InputConstants.UNKNOWN), Optional::ofNullable).forGetter(KeybindingCombination::getAttributeKey),
			KEY_CODEC.optionalFieldOf("key").xmap((o) -> o.orElse(InputConstants.UNKNOWN), Optional::ofNullable).forGetter(KeybindingCombination::getKey)
	).apply(instance, KeybindingCombination::new));
	private static final Component COLLECT_TEXT = TextUtils.literal(" + ");
	private static final Set<Integer> ATTRIBUTE_KEY_IDS = Set.of(
			//? if >=26.3 {
			InputConstants.KEY_RGUI,
			InputConstants.KEY_LGUI,
			//?} else {
			/*InputConstants.KEY_RSUPER,
			InputConstants.KEY_LSUPER,
			*///?}

			InputConstants.KEY_LALT,
			InputConstants.KEY_LCONTROL,
			InputConstants.KEY_LSHIFT,
			InputConstants.KEY_RALT,
			InputConstants.KEY_RCONTROL,
			InputConstants.KEY_RSHIFT,
			InputConstants.KEY_TAB,
			InputConstants.KEY_CAPSLOCK
	);

	@Nullable
	private InputConstants.Key attributeKey;
	@Nullable
	private InputConstants.Key key;

	public static boolean isAttributeKey(int keyId) {
		return ATTRIBUTE_KEY_IDS.contains(keyId);
	}

	private static Key getKeyByName(String name) {
		try {
			return InputConstants.getKey(name);
		} catch (IllegalArgumentException e) {
			return InputConstants.UNKNOWN;
		}
	}

	public void setAttributeKey(@Nullable Key attributeKey) {
		if (InputConstants.UNKNOWN.equals(attributeKey)) {
			this.attributeKey = null;
			return;
		}
		this.attributeKey = attributeKey;
	}

	public void setKey(@Nullable Key key) {
		if (InputConstants.UNKNOWN.equals(key)) {
			this.key = null;
			return;
		}
		this.key = key;
	}

	public boolean isComplete() {
		return this.attributeKey != null && this.key != null;
	}

	public List<Key> getKeys() {
		return Stream.of(this.getKey(), this.getAttributeKey()).filter(Objects::nonNull).filter((key) -> !InputConstants.UNKNOWN.equals(key)).toList();
	}

	public Component getCombinationLocalizedComponent(boolean asFinished) {
		if (this.attributeKey != null) {
			MutableComponent component = this.attributeKey.getDisplayName().copy();
			if (!asFinished) {
				component.append(COLLECT_TEXT);
			}
			if (this.key != null) {
				if (asFinished) {
					component.append(COLLECT_TEXT);
				}
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
		if (!(o instanceof KeybindingCombination that)) {
			return false;
		}
		return Objects.equals(this.attributeKey, that.attributeKey) && Objects.equals(this.key, that.key);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.attributeKey, this.key);
	}
}
