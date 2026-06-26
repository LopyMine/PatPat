package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;

//? if fabric {
import net.lopymine.patpat.entrypoint.impl.fabric.loader.FabricServerModLoader;
//?} elif neoforge {
/*import net.lopymine.patpat.entrypoint.impl.neoforge.loader.NeoForgeServerModLoader;
 *///?}

public class ServerMultiLoader {

	//? if fabric {
	private static final IServerModLoader LOADER = new FabricServerModLoader();
	//?} elif neoforge {
	/*private static final IServerModLoader LOADER = new NeoForgeServerModLoader();
	*///?}

	public static IServerModLoader getInstance() {
		return LOADER;
	}
}
