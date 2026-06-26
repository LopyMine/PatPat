package net.lopymine.patpat.utils;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class SoundUtils {

	private SoundUtils() {
		throw new IllegalStateException("Utility class");
	}

	@NotNull
	public static SoundEvent getSoundEvent(@NotNull String value) {
		Identifier id = RLUtils.modId(value);
		return SoundEvent.createVariableRangeEvent(id);
	}

	@NotNull
	public static String getLocation(@NotNull SoundEvent sound) {
		return sound.location().toString();
	}
}
