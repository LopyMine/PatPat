package net.lopymine.patpat.utils;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.extension.GameProfileExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

import net.lopymine.patpat.client.config.list.PatPatClientPlayerListConfig;

import java.util.*;
import java.util.stream.Stream;

@ExtensionMethod(GameProfileExtension.class)
public class ClientNetworkUtils {

	private ClientNetworkUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<String> getOnlinePlayersFromUuids() {
		ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
		if (networkHandler == null) {
			return Collections.emptyList();
		}

		return PatPatClientPlayerListConfig.getInstance().getValues().entrySet().stream().flatMap(entry -> {
			PlayerInfo playerListEntry = networkHandler.getPlayerInfo(entry.getKey());
			if (playerListEntry == null) {
				return Stream.of(entry.getValue());
			}
			return Stream.of(playerListEntry.getProfile().getName());
		}).toList();
	}
}
