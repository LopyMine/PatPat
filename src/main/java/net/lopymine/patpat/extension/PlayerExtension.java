package net.lopymine.patpat.extension;

import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

import net.minecraft.server.players.NameAndId;

public class PlayerExtension {

	private PlayerExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static boolean hasPermission(ServerPlayer player, String permission) {
		return ServerMultiLoader.getInstance().hasPermission(player, permission);
	}

	public static CompletableFuture<Boolean> hasPermission(
			 NameAndId  profile,
			MinecraftServer server,
			String permission
	) {
		return ServerMultiLoader.getInstance().hasOfflinePermission(profile, server, permission);
	}

}
