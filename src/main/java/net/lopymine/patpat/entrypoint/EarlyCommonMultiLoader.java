package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader;

//? if fabric {
import net.lopymine.patpat.entrypoint.impl.fabric.loader.FabricEarlyModLoader;
//?} elif neoforge {
/*import net.lopymine.patpat.entrypoint.impl.neoforge.loader.NeoForgeEarlyModLoader;
*///?}

public class EarlyCommonMultiLoader {

	//? if fabric {
	private static final IEarlyCommonModLoader LOADER = new FabricEarlyModLoader();
	//?} elif neoforge {
	/*private static final IEarlyCommonModLoader LOADER = new NeoForgeEarlyModLoader();;
	*///?}

	public static IEarlyCommonModLoader getInstance() {
		return LOADER;
	}
}
