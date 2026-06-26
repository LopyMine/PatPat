package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.client.IClientModLoader;

//? if fabric {
import net.lopymine.patpat.entrypoint.impl.fabric.loader.FabricClientModLoader;
//?} elif neoforge {
/*import net.lopymine.patpat.entrypoint.impl.neoforge.loader.NeoForgeClientModLoader;
 *///?}

public class ClientMultiLoader {

	//? if fabric {
	private static final IClientModLoader LOADER = new FabricClientModLoader();
	//?} elif neoforge {
	/*private static final IClientModLoader LOADER = new NeoForgeClientModLoader();
	*///?}

	public static IClientModLoader getInstance() {
		return LOADER;
	}
}
