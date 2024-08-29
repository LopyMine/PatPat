package net.lopymine.patpat.extension;

import net.minecraft.text.Text;

public class TextExtension {

	private TextExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static String asString(Text text) {
		return text.getString().replaceAll("§[A-Za-z0-9]", "");
	}
}
