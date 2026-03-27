package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.loader.IEarlyCommonModLoader;

//? if fabric {
import net.lopymine.patpat.entrypoint.impl.fabric.loader.FabricEarlyModLoader;
//?} elif neoforge {
/*import net.lopymine.patpat.entrypoint.impl.neoforge.loader.NeoForgeEarlyModLoader;
*///?} elif forge {
/*import net.lopymine.patpat.entrypoint.impl.forge.loader.ForgeEarlyCommonModLoader;
*///?}

public class EarlyCommonMultiLoader {

	//? if fabric {
	private static final IEarlyCommonModLoader LOADER = new FabricEarlyModLoader();
	//?} elif neoforge {
	/*private static final IEarlyCommonModLoader LOADER = new NeoForgeEarlyModLoader();
	*///?} elif forge {
	/*private static final IEarlyCommonModLoader LOADER = new ForgeEarlyCommonModLoader();
	*///?}

	public static IEarlyCommonModLoader getInstance() {
		return LOADER;
	}
}
