package net.lopymine.patpat.client.packet;

import lombok.Getter;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.compat.LoadedMods;

public class PatPatClientProxLibManager {

	private PatPatClientProxLibManager() {
		throw new IllegalStateException("Manager class");
	}

	@Getter
	private static boolean enabled;

	public static void register() {
		PatPatClientProxLibManager.setEnabled(LoadedMods.PROX_LIB_MOD_LOADED);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if(PatPatClientConfig.getInstance().getServerConfig().isProxLibEnabled()){
				PatPatClientProxLibManager.enabled = true;
			}
		});
	}

	public static void setEnabled(boolean enabled) {
		PatPatClientProxLibManager.enabled = enabled;
		PatPatClient.LOGGER.debug(enabled ? "ProxLib Enabled" : "ProxLib Disabled");
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
