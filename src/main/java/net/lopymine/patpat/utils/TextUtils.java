package net.lopymine.patpat.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import net.lopymine.patpat.PatPat;

public class TextUtils {

	private TextUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static MutableComponent translatable(String key, Object... args) {
		//? >=1.19 {
		return Component.translatable(key, args);
		//?} else {
		/*return new TranslatableText(key, args);
		*///?}
	}

	public static MutableComponent literal(String key) {
		//? >=1.19 {
		return Component.literal(key);
		//?} else {
		/*return new LiteralText(key);
		*///?}
	}

	public static Component of(String key) {
		return Component.nullToEmpty(key);
	}

	public static MutableComponent text(String path, Object... args) {
		return TextUtils.translatable(String.format("%s.%s", PatPat.MOD_ID, path), args);
	}
}
