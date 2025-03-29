package net.lopymine.patpat.manager.server.command;

import net.minecraft.server.network.ServerPlayerEntity;

import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.*;

import java.util.UUID;

public class ListManager {

	private ListManager() {
		throw new IllegalStateException("Manager class");
	}

	public static boolean canPat(ServerPlayerEntity player) {
		return canPat(player.getUuid());
	}

	public static boolean canPat(UUID uuid) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PlayerListConfig playerListConfig = PlayerListConfig.getInstance();
		if (config.getListMode() == ListMode.DISABLED) {
			return true;
		}
		if (config.getListMode() == ListMode.WHITELIST && !playerListConfig.contains(uuid)) {
			return false;
		}
		return config.getListMode() != ListMode.BLACKLIST || !playerListConfig.contains(uuid);
	}
}
