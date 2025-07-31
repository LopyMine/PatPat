package net.lopymine.patpat.client.packet;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.ProximityPacketServersWhitelistConfig;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.compat.flashback.FlashbackManager;
import net.lopymine.patpat.compat.replaymod.ReplayModManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class PatPatClientProxLibManager {

	private PatPatClientProxLibManager() {
		throw new IllegalStateException("Manager class");
	}

	public static final PatLogger LOGGER = PatPatClient.LOGGER.extend("ProxLibManager");

	private static boolean enabled;

	public static void register() {
		PatPatClientProxLibManager.reset();
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if (PatPatClientConfig.getInstance().getProximityPacketsConfig().isProximityPacketsEnabled()) {
				PatPatClientProxLibManager.reset();
			}
		});
	}

	private static void reset() {
		PatPatClientProxLibManager.setEnabled(LoadedMods.PROX_LIB_MOD_LOADED);
	}

	// TODO: rewrite this, use getCurrentServer when call isEnabled is not good solution
	public static boolean isEnabled() {
		if (!enabled) {
			return false;
		}
		if (!PatPatClientConfig.getInstance().getProximityPacketsConfig().isProximityPacketsEnabled()) {
			return false;
		}
		ServerData serverData = Minecraft.getInstance().getCurrentServer();
		if (serverData == null) {
			return false;
		}
		return PatPatClientConfig.getInstance().getProximityPacketsConfig().isBlacklist()
				!= ProximityPacketServersWhitelistConfig.getInstance().isWhitelisted(serverData.ip);
	}

	public static void setEnabled(boolean enabled) {
		PatPatClientProxLibManager.enabled = enabled;
		LOGGER.debug(enabled ? "Proximity Packets Enabled" : "Proximity Packets Disabled");
	}

	public static void setEnabledIfNotInReplay(boolean enabled) {
		if (FlashbackManager.isInReplay() || ReplayModManager.isInReplay()) {
			return;
		}
		PatPatClientProxLibManager.setEnabled(enabled);
	}

	public static void disableIfEnabledBecauseReceivedPacketFromServer() {
		if (isEnabled()) {
			LOGGER.debug("--------------------------------------");
			LOGGER.debug("Received pat packet from server with enabled Proximity Packets, looks like server has old PatPat Plugin/Mod installed. Automatically disabling Proximity Packets to avoid packet duplication...");
			LOGGER.debug("--------------------------------------");
			PatPatClientProxLibManager.setEnabled(false);
		}
	}
}
