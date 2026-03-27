package net.lopymine.patpat.compat;

import net.lopymine.patpat.entrypoint.*;
import net.lopymine.patpat.client.PatPatClient;

public final class LoadedMods {

	public static final boolean REPLAY_MOD_LOADED = EarlyCommonMultiLoader.getInstance().isModLoaded("replaymod");
	public static final boolean FLASHBACK_MOD_LOADED = EarlyCommonMultiLoader.getInstance().isModLoaded("flashback");
	public static final boolean PROX_LIB_MOD_LOADED = EarlyCommonMultiLoader.getInstance().isModLoaded("proxlib");
	public static final boolean IRIS_LOADED = EarlyCommonMultiLoader.getInstance().isModLoaded("iris");

	public static void onInitialize() {
		sendDebugLog(REPLAY_MOD_LOADED, "Replay Mod");
		sendDebugLog(FLASHBACK_MOD_LOADED, "Flashback Mod");
		sendDebugLog(PROX_LIB_MOD_LOADED, "ProxLib");
		sendDebugLog(IRIS_LOADED, "Iris");
	}

	private static void sendDebugLog(boolean modLoaded, String modId) {
		PatPatClient.LOGGER.debug("{} Loaded: {}", modId, modLoaded);
	}
}
