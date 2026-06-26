package net.lopymine.patpat.client.config.sub;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import net.lopymine.patpat.client.PatPatClient;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.*;

public enum InputType implements StringRepresentable {

	KEYSYM,
	SCANCODE,
	MOUSE;

	public static final Codec<InputType> CODEC = StringRepresentable.fromEnum(InputType::values);

	@Nullable
	private static InputType byName(String name) {
		try {
			return InputType.valueOf(name.toUpperCase());
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to get enum by name \"{}\":", name, e);
			return null;
		}
	}

	public static InputType of(InputConstants.Type type) {
		return switch (type) {
			case KEYSYM -> KEYSYM;
			case SCANCODE -> SCANCODE;
			case MOUSE -> MOUSE;
		};
	}

	public InputConstants.Type toVanillaType() {
		return switch (this) {
			case KEYSYM -> InputConstants.Type.KEYSYM;
			case SCANCODE -> InputConstants.Type.SCANCODE;
			case MOUSE -> InputConstants.Type.MOUSE;
		};
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name().toLowerCase();
	}

}

