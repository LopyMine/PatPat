package net.lopymine.patpat.client.keybinding;

import lombok.*;
import net.minecraft.network.chat.*;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.patpat.client.config.InputType;

import java.util.*;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeybindingCombination {

	private static final Component COLLECT_TEXT = Component.literal(" + ");

	public static final Codec<Key> KEY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.optionalFieldOf("id").xmap((o) -> o.orElse(InputConstants.UNKNOWN.getValue()), Optional::ofNullable).forGetter(Key::getValue),
			InputType.CODEC.optionalFieldOf("type").xmap((o) -> o.orElse(InputType.of(InputConstants.UNKNOWN.getType())), Optional::ofNullable).forGetter((key) -> InputType.of(key.getType()))
	).apply(instance, (id, type) -> type.toVanillaType().getOrCreate(id)));

	public static final Codec<KeybindingCombination> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			KEY_CODEC.optionalFieldOf("attributeKey").xmap((o) -> o.orElse(InputConstants.UNKNOWN), Optional::ofNullable).forGetter(KeybindingCombination::getAttributeKey),
			KEY_CODEC.optionalFieldOf("key").xmap((o) -> o.orElse(InputConstants.UNKNOWN), Optional::ofNullable).forGetter(KeybindingCombination::getKey)
	).apply(instance, KeybindingCombination::new));

	@Nullable
	private InputConstants.Key attributeKey;
	@Nullable
	private InputConstants.Key key;

	public boolean isComplete() {
		return this.attributeKey != null && this.key != null;
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
		KeybindingCombination that = (KeybindingCombination) o;
		return Objects.equals(this.attributeKey, that.attributeKey) && Objects.equals(this.key, that.key);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.attributeKey, this.key);
	}
}
