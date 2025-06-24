package net.lopymine.patpat.client.keybinding;

import lombok.*;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.network.chat.*;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.config.sub.InputType;

import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeybindingCombination {

	private static final Component COLLECT_TEXT = TextUtils.literal(" + ");

	public static final Codec<Key> KEY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.optionalFieldOf("id").xmap((o) -> o.orElse(-1), Optional::ofNullable).forGetter(Key::getValue),
			InputType.CODEC.optionalFieldOf("type").xmap((o) -> o.orElse(InputType.of(InputConstants.UNKNOWN.getType())), Optional::ofNullable).forGetter((key) -> InputType.of(key.getType()))
	).apply(instance, (id, type) -> type.toVanillaType().getOrCreate(id)));

	public static final Codec<KeybindingCombination> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			KEY_CODEC.optionalFieldOf("attributeKey").xmap((o) -> o.orElse(InputConstants.UNKNOWN), Optional::ofNullable).forGetter(KeybindingCombination::getAttributeKey),
			KEY_CODEC.optionalFieldOf("key").xmap((o) -> o.orElse(InputConstants.UNKNOWN), Optional::ofNullable).forGetter(KeybindingCombination::getKey)
	).apply(instance, KeybindingCombination::new));

	private static final Set<Integer> ATTRIBUTE_KEY_IDS = Set.of(
			GLFW.GLFW_KEY_LEFT_ALT,
			GLFW.GLFW_KEY_LEFT_CONTROL,
			GLFW.GLFW_KEY_LEFT_SHIFT,
			GLFW.GLFW_KEY_LEFT_SUPER,
			GLFW.GLFW_KEY_RIGHT_ALT,
			GLFW.GLFW_KEY_RIGHT_CONTROL,
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			GLFW.GLFW_KEY_RIGHT_SUPER,
			GLFW.GLFW_KEY_TAB,
			GLFW.GLFW_KEY_CAPS_LOCK
	);

	@Nullable
	private InputConstants.Key attributeKey;
	@Nullable
	private InputConstants.Key key;

	public static boolean isAttributeKey(int keyId) {
		return ATTRIBUTE_KEY_IDS.contains(keyId);
	}

	public void setAttributeKey(@Nullable Key attributeKey) {
		if (attributeKey != null && attributeKey.getValue() == -1) {
			this.attributeKey = null;
			return;
		}
		this.attributeKey = attributeKey;
	}

	public void setKey(@Nullable Key key) {
		if (key != null && key.getValue() == -1) {
			this.key = null;
			return;
		}
		this.key = key;
	}

	public boolean isComplete() {
		return this.attributeKey != null && this.key != null;
	}

	public List<Key> getKeys() {
		return Stream.of(this.getKey(), this.getAttributeKey()).filter(Objects::nonNull).filter((key) -> key.getValue() != -1).toList();
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
