package net.lopymine.patpat.client.packet;

import lombok.Getter;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.LoadedMods;

public class PatPatClientProxLibManager {

	@Getter
	private static boolean enabled;

	public static void register() {
		PatPatClientProxLibManager.setEnabled(LoadedMods.PROX_LIB_MOD_LOADED);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			PatPatClientProxLibManager.enabled = true;
		});
	}

	public static void setEnabled(boolean enabled) {
		PatPatClientProxLibManager.enabled = enabled;
		if (enabled) {
			PatPatClient.LOGGER.debug("ProxLib Enabled");
		} else {
			PatPatClient.LOGGER.debug("ProxLib Disabled");
		}
	}
}
