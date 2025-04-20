package net.lopymine.patpat.server.ratelimit;

import net.minecraft.server.network.ServerPlayerEntity;

import net.lopymine.patpat.client.config.resourcepack.ListMode;

import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;

import java.util.UUID;

public class PatPatServerListManager {

	private PatPatServerListManager() {
		throw new IllegalStateException("Manager class");
	}

	public static boolean canPat(ServerPlayerEntity player) {
		return canPat(player.getUuid());
	}

	public static boolean canPat(UUID uuid) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerPlayerListConfig playerListConfig = PatPatServerPlayerListConfig.getInstance();
		if (config.getListMode() == ListMode.DISABLED) {
			return true;
		}
		if (config.getListMode() == ListMode.WHITELIST && !playerListConfig.contains(uuid)) {
			return false;
		}
		return config.getListMode() != ListMode.BLACKLIST || !playerListConfig.contains(uuid);
	}
}
