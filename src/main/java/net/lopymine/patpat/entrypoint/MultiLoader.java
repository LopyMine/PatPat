package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.IModLoader;
import net.lopymine.patpat.entrypoint.neoforge.loader.NeoForgeModLoader;

public class MultiLoader {

	//? if fabric {
	/*private static final IModLoader LOADER = new net.lopymine.patpat.entrypoint.fabric.loader.FabricModLoader();
	*///?} elif neoforge {
	/*private static final IModLoader LOADER = new NeoForgeModLoader();
	*///?} elif forge {
	private static final IModLoader LOADER = new s();
	//?}

	public static IModLoader getInstance() {
		return LOADER;
	}
}
