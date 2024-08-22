package net.lopymine.patpat.utils;

import net.minecraft.text.*;

public class TextUtils {

	private TextUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static MutableText translatable(String key, Object... args) {
		//? >=1.19 {
		return Text.translatable(key, args);
		//?} else {
		/*return new TranslatableText(key, args);
		*///?}
	}

	public static MutableText literal(String key) {
		//? >=1.19 {
		return Text.literal(key);
		//?} else {
		/*return new LiteralText(key);
		*///?}
	}

	public static Text of(String key) {
		return Text.of(key);
	}
}
