package net.lopymine.patpat.utils;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.*;

public class SoundUtils {

	private SoundUtils() {
		throw new IllegalStateException("Utility class");
	}

	@NotNull
	public static SoundEvent getSoundEvent(@NotNull String value) {
		Identifier id = RLUtils.modId(value);
		return /*? >=1.19.3 {*/SoundEvent.createVariableRangeEvent(id)/*?} else {*/ /*new SoundEvent(id)*//*?}*/;
	}

	@NotNull
	public static String getLocation(@NotNull SoundEvent sound) {
		return sound./*? <1.21.2 {*//*getLocation()*//*?} else {*/location()/*?}*/.toString();
	}
}
