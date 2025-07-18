package net.lopymine.patpat.extension;

import net.minecraft.network.chat.Component;

public class TextExtension {

	private TextExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static String asString(Component text) {
		return text.getString().replaceAll("§[A-Za-z0-9]", "");
	}
}
