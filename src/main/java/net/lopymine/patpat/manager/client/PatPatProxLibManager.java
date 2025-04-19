package net.lopymine.patpat.manager.client;

import lombok.Getter;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.LoadedMods;

public class PatPatProxLibManager {

	@Getter
	private static boolean enabled;

	public static void register() {
		PatPatProxLibManager.setEnabled(LoadedMods.PROX_LIB_MOD_LOADED);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			PatPatProxLibManager.enabled = true;
		});
	}

	public static void setEnabled(boolean enabled) {
		PatPatProxLibManager.enabled = enabled;
		if (enabled) {
			PatPatClient.LOGGER.debug("ProxLib Enabled");
		} else {
			PatPatClient.LOGGER.debug("ProxLib Disabled");
		}
	}
}
