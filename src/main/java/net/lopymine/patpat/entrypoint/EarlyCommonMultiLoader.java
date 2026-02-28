package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader;

public class EarlyCommonMultiLoader {

	//? if fabric {
	/*private static final IEarlyCommonModLoader LOADER = new net.lopymine.patpat.entrypoint.fabric.loader.FabricEarlyModLoader();
	*///?} elif neoforge {
	/*private static final IEarlyCommonModLoader LOADER = new net.lopymine.patpat.entrypoint.neoforge.loader.NeoForgeEarlyModLoader();
	*///?} elif forge {
	private static final IEarlyCommonModLoader LOADER = new net.lopymine.patpat.entrypoint.forge.loader.ForgeEarlyCommonModLoader();
	//?}

	public static IEarlyCommonModLoader getInstance() {
		return LOADER;
	}
}
