package net.lopymine.patpat.utils;

import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import static net.lopymine.patpat.PatPat.MOD_ID;

public class IdentifierUtils {

	private static final String TEXTURES_PATH = "textures/";

	private IdentifierUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Identifier id(@NotNull String path) {
		if (path.contains(":")) {
			return new Identifier(path);
		}
		return new Identifier(MOD_ID, path);
	}

	public static Identifier textureId(@NotNull String path) {
		return typeId(path, TEXTURES_PATH);
	}

	public static Identifier typeId(@NotNull String path, @NotNull String type) {
		String namespace = MOD_ID;
		String[] split = path.split(":");
		if (split.length >= 2) {
			namespace = split[0];
			path = split[1];
		}
		if (!path.startsWith(type)) {
			path = type + path;
		}
		return new Identifier(namespace, path);
	}
}
