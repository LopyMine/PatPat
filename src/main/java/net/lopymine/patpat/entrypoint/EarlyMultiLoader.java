package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.IEarlyModLoader;

public class EarlyMultiLoader {

	//? if fabric {
	/*private static final IEarlyModLoader LOADER = new net.lopymine.patpat.entrypoint.fabric.loader.FabricEarlyModLoader();
	*///?} elif neoforge {
	/*private static final IEarlyModLoader LOADER = new net.lopymine.patpat.entrypoint.neoforge.loader.NeoForgeEarlyModLoader();
	*///?} elif forge {
	private static final IEarlyModLoader LOADER = new net.lopymine.patpat.entrypoint.forge.loader.ForgeEarlyModLoader();
	//?}

	public static IEarlyModLoader getInstance() {
		return LOADER;
	}
}
