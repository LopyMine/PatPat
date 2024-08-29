package net.lopymine.patpat.utils;

import net.minecraft.util.Identifier;

import net.lopymine.patpat.PatPat;

import org.jetbrains.annotations.*;

public class IdentifierUtils {

	private static final String TEXTURES_PATH = "textures/";

	private IdentifierUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Identifier id(@NotNull String id) {
		return IdentifierUtils.typeId(id, null);
	}

	public static Identifier textureId(@NotNull String id) {
		return IdentifierUtils.typeId(id, TEXTURES_PATH);
	}

	public static Identifier typeId(@NotNull String id, @Nullable String type) {
		String namespace = PatPat.MOD_ID;
		String path = id;
		String[] split = path.split(":");
		if (split.length >= 2) {
			namespace = split[0];
			path      = split[1];
		}
		if (type != null && !path.startsWith(type)) {
			path = type + path;
		}
		//? >=1.19 {
		return Identifier.of(namespace, path);
		//?} else {
		/*return new Identifier(namespace, path);
		*///?}
	}
}
