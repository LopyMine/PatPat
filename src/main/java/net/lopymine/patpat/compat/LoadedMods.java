package net.lopymine.patpat.compat;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.patpat.client.PatPatClient;

public final class LoadedMods {
	public static boolean REPLAY_MOD_LOADED = FabricLoader.getInstance().isModLoaded("replaymod");

	public static void onInitialize() {
		sendDebugLog(REPLAY_MOD_LOADED, "Replay Mod");
	}

	private static void sendDebugLog(boolean modLoaded, String modId) {
		if (PatPatClient.getConfig().isDebugLogEnabled()) {
			PatPatClient.LOGGER.info("{} Loaded: {}", modId, modLoaded);
		}
	}
}
