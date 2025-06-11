package net.lopymine.patpat.client.config;

import net.minecraft.util.StringRepresentable;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;

import org.jetbrains.annotations.NotNull;

public enum InputType implements StringRepresentable {

	KEYSYM,
	SCANCODE,
	MOUSE;

	public static final Codec<InputType> CODEC = StringRepresentable.fromValues(InputType::values);

	public InputConstants.Type toVanillaType() {
		return switch (this) {
			case KEYSYM -> InputConstants.Type.KEYSYM;
			case SCANCODE -> InputConstants.Type.SCANCODE;
			case MOUSE -> InputConstants.Type.MOUSE;
		};
	}

	public static InputType of(InputConstants.Type type) {
		return switch (type) {
			case InputConstants.Type.KEYSYM -> KEYSYM;
			case InputConstants.Type.SCANCODE -> SCANCODE;
			case InputConstants.Type.MOUSE -> MOUSE;
		};
	}


	@Override
	public @NotNull String getSerializedName() {
		return this.name().toLowerCase();
	}

}

