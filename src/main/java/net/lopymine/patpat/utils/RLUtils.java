package net.lopymine.patpat.utils;

import net.lopymine.patpat.PatPat;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.*;

public class RLUtils {

	private RLUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Identifier parse(String string){
		/*? if >1.20.6 {*/
		return Identifier.parse(string);
		/*?} else {*/
		/*return new Identifier(string);
		 *//*?}*/
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

		//? >=1.21 {
		return Identifier.fromNamespaceAndPath(name, location);
		//?} else {
		/*return new Identifier(name, location);
		*///?}
	}
}
