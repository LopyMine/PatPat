package net.lopymine.patpat.compat;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.client.PatPatClient;

public final class LoadedMods {
	public static final boolean REPLAY_MOD_LOADED = FabricLoader.getInstance().isModLoaded("replaymod");
	public static final boolean FLASHBACK_MOD_LOADED = FabricLoader.getInstance().isModLoaded("flashback");

	public static void onInitialize() {
		sendDebugLog(REPLAY_MOD_LOADED, "Replay Mod");
		sendDebugLog(FLASHBACK_MOD_LOADED, "Flashback Mod");
	}

	private static void sendDebugLog(boolean modLoaded, String modId) {
		PatPatClient.LOGGER.debug("{} Loaded: {}", modId, modLoaded);
	}
}
