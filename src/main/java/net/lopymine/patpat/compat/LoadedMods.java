package net.lopymine.patpat.compat;

import net.fabricmc.loader.api.FabricLoader;

public final class LoadedMods {
	public static boolean REPLAY_MOD_LOADED = FabricLoader.getInstance().isModLoaded("replaymod");

	public static void onInitialize() {
		// NO-OP
	}
}
