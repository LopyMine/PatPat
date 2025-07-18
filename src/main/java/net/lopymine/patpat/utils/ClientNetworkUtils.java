package net.lopymine.patpat.utils;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

import net.lopymine.patpat.client.config.PatPatClientPlayerListConfig;

import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.*;

public class ClientNetworkUtils {

	private ClientNetworkUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<String> getOnlinePlayersFromUuids(@Nullable ClientPacketListener networkHandler) {
		if (networkHandler == null) {
			return Collections.emptyList();
		}
		PatPatClientPlayerListConfig playerListConfig = PatPatClientPlayerListConfig.getInstance();
		if (playerListConfig == null) {
			return Collections.emptyList();
		}
		return playerListConfig.getMap().entrySet().stream().flatMap(entry -> {
			PlayerInfo playerListEntry = networkHandler.getPlayerInfo(entry.getKey());
			if (playerListEntry == null) {
				return Stream.of(entry.getValue());
			}
			return Stream.of(playerListEntry.getProfile().getName());
		}).toList();
	}
}
