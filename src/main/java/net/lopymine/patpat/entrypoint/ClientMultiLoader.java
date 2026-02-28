package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;

public class ClientMultiLoader {

	//? if fabric {
	/*private static final IClientModLoader LOADER = new net.lopymine.patpat.entrypoint.fabric.loader.FabricClientModLoader();
	*///?} elif neoforge {
	/*private static final IClientModLoader LOADER = new net.lopymine.patpat.entrypoint.neoforge.loader.NeoForgeClientModLoader();
	*///?} elif forge {
	private static final IClientModLoader LOADER = new net.lopymine.patpat.entrypoint.forge.loader.ForgeClientModLoader();
	//?}

	public static IClientModLoader getInstance() {
		return LOADER;
	}
}
