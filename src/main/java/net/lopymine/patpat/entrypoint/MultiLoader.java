package net.lopymine.patpat.entrypoint;

import net.lopymine.patpat.entrypoint.forge.ForgeModLoader;

public class MultiLoader {

	private static final IModLoader LOADER = new ForgeModLoader();

	public static IModLoader getInstance() {
		return LOADER;
	}
}
