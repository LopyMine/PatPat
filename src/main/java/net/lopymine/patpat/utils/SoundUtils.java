package net.lopymine.patpat.utils;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

public class SoundUtils {

	private SoundUtils() {
		throw new IllegalStateException("Utility class");
	}

	@NotNull
	public static SoundEvent getSoundEvent(@NotNull String value) {
		Identifier identifier = IdentifierUtils.id(value);
		return SoundEvent.of(identifier);
	}

	@NotNull
	public static String getTypeId(@NotNull SoundEvent sound) {
		return sound.getId().toString();
	}
}
