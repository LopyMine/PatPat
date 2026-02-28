package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;

public class ServerMultiLoader {

	//? if fabric {
	/*private static final IServerModLoader LOADER = new net.lopymine.patpat.entrypoint.fabric.loader.FabricServerModLoader();
	*///?} elif neoforge {
	/*private static final IServerModLoader LOADER = new net.lopymine.patpat.entrypoint.neoforge.loader.NeoForgeServerModLoader();
	*///?} elif forge {
	private static final IServerModLoader LOADER = new net.lopymine.patpat.entrypoint.forge.loader.ForgeServerModLoader();
	//?}

	public static IServerModLoader getInstance() {
		return LOADER;
	}
}
