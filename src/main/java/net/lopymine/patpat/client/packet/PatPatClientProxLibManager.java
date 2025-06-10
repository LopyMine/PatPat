package net.lopymine.patpat.client.packet;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.compat.flashback.FlashbackManager;
import net.lopymine.patpat.compat.replaymod.ReplayModManager;

public class PatPatClientProxLibManager {

	private PatPatClientProxLibManager() {
		throw new IllegalStateException("Manager class");
	}

	private static boolean enabled;

	public static void register() {
		PatPatClientProxLibManager.reset();
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if (PatPatClientConfig.getInstance().getProximityPacketsConfig().isProximityPacketsEnabled()){
				PatPatClientProxLibManager.reset();
			}
		});
	}

	private static void reset() {
		PatPatClientProxLibManager.setEnabled(LoadedMods.PROX_LIB_MOD_LOADED);
	}

	public static boolean isEnabled() {
		return enabled && PatPatClientConfig.getInstance().getProximityPacketsConfig().isProximityPacketsEnabled();
	}

	public static void setEnabled(boolean enabled) {
		PatPatClientProxLibManager.enabled = enabled;
		PatPatClient.LOGGER.debug(enabled ? "ProxLib Enabled" : "ProxLib Disabled");
	}

	public static void setEnabledIfNotInReplay(boolean enabled) {
		if (FlashbackManager.isInReplay() || ReplayModManager.isInReplay()) {
			return;
		}
		PatPatClientProxLibManager.setEnabled(enabled);
	}

	public static void disableIfEnabledBecauseReceivedPacketFromServer() {
		if (isEnabled()) {
			PatPatClient.LOGGER.debug("--------------------------------------");
			PatPatClient.LOGGER.debug("Received pat packet from server with enabled ProxLib, looks like server has old PatPat Plugin/Mod installed. Automatically disabling ProxLib to avoid packet duplication...");
			PatPatClient.LOGGER.debug("--------------------------------------");
			PatPatClientProxLibManager.setEnabled(false);
		}
	}
}
