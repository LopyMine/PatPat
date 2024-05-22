package net.lopymine.patpat.utils;

import net.minecraft.client.network.*;

import net.lopymine.patpat.config.ListConfig;

import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.*;

public class ClientNetworkUtils {
	private ClientNetworkUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<String> getOnlinePlayersFromUuids(@Nullable ClientPlayNetworkHandler networkHandler, @NotNull ListConfig config) {
		if (networkHandler == null) {
			return List.of();
		}
		return config.getPlayers().entrySet().stream().flatMap(entry -> {
			PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(entry.getKey());
			if (playerListEntry == null) {
				return Stream.of(entry.getValue());
			}
			return Stream.of(playerListEntry.getProfile().getName());
		}).toList();
	}
}
