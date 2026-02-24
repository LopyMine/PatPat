package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.IModLoader;

public class MultiLoader {

	//? if fabric {
	/*private static final IModLoader LOADER = new net.lopymine.patpat.entrypoint.fabric.loader.FabricModLoader();
	*///?} elif neoforge {
	/*private static final IModLoader LOADER = new net.lopymine.patpat.entrypoint.neoforge.loaderNeoForgeModLoader();
	*///?} elif forge {
	private static final IModLoader LOADER = new net.lopymine.patpat.entrypoint.forge.loader.ForgeModLoader();
	//?}

	public static IModLoader getInstance() {
		return LOADER;
	}
}
