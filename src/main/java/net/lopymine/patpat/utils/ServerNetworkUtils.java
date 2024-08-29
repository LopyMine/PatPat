package net.lopymine.patpat.utils;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import net.lopymine.patpat.config.server.PatPatServerConfig;

import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class ServerNetworkUtils {

	private ServerNetworkUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<String> getPlayersFromList(@NotNull PlayerManager playerManager, @NotNull PatPatServerConfig config) {
		return config.getList().entrySet().stream().flatMap(entry -> {
			ServerPlayerEntity entity = playerManager.getPlayer(entry.getKey());
			if (entity == null) {
				return Stream.of(entry.getValue());
			}
			return Stream.of(entity.getGameProfile().getName());
		}).toList();
	}
}
