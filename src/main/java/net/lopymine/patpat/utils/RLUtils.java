package net.lopymine.patpat.utils;

import net.lopymine.patpat.PatPat;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.*;

public class RLUtils {

	private RLUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static ResourceLocation parse(String string){
		/*? if >1.20.6 {*/
		/*return ResourceLocation.parse(string);
		*//*?} else {*/
		return new ResourceLocation(string);
		 /*?}*/
	}

	public static ResourceLocation modId(@NotNull String path) {
		return RLUtils.id(PatPat.MOD_ID, path);
	}

	public static ResourceLocation vanillaId(@NotNull String path) {
		return RLUtils.id("minecraft", path);
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
		*///?} else {
		return new ResourceLocation(name, location);
		//?}
	}
}
