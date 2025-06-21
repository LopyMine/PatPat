package net.lopymine.patpat.utils;

import net.lopymine.patpat.PatPat;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.*;

public class IdentifierUtils {

	private IdentifierUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static ResourceLocation modId(@NotNull String path) {
		return IdentifierUtils.id(PatPat.MOD_ID, path);
	}

	public static ResourceLocation vanillaId(@NotNull String path) {
		return IdentifierUtils.id("minecraft", path);
	}

	public static ResourceLocation id(String namespace, String path) {
		String name = namespace;
		String location = path;

		String[] split = path.split(":");
		if (split.length >= 2) {
			name     = split[0];
			location = split[1];
		}

		//? >=1.21 {
		/*return ResourceLocation.fromNamespaceAndPath(name, location);
		*///?} elif >=1.19 {
		return new ResourceLocation(name, location);
		//?} else {
		/*return new Identifier(name, location);
		*///?}
	}
}
