package net.lopymine.patpat.utils;

import net.lopymine.patpat.PatPat;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RLUtils {

	private RLUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Identifier parse(String string) {
		return Identifier.parse(string);
	}

	public static Identifier modId(@NotNull String path) {
		return RLUtils.id(PatPat.MOD_ID, path);
	}

	public static Identifier vanillaId(@NotNull String path) {
		return RLUtils.id("minecraft", path);
	}

	public static Identifier id(String namespace, String path) {
		String name = namespace;
		String location = path;

		String[] split = path.split(":");
		if (split.length >= 2) {
			name     = split[0];
			location = split[1];
		}

		return Identifier.fromNamespaceAndPath(name, location);
	}
}
