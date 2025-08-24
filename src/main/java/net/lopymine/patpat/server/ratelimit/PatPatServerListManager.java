package net.lopymine.patpat.server.ratelimit;

import net.lopymine.patpat.client.config.resourcepack.ListMode;

import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.list.PatPatServerPlayerListConfig;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public class PatPatServerListManager {

	private PatPatServerListManager() {
		throw new IllegalStateException("Manager class");
	}

	public static boolean canPat(ServerPlayer player) {
		return canPat(player.getUUID());
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
