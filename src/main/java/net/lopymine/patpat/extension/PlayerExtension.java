package net.lopymine.patpat.extension;

import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

//? if >=1.21.9 {
import net.minecraft.server.players.NameAndId;
//?} else {
/*import com.mojang.authlib.GameProfile;
 *///?}

public class PlayerExtension {

	private PlayerExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static boolean hasPermission(ServerPlayer player, String permission) {
		return ServerMultiLoader.getInstance().hasPermission(player, permission);
	}

	public static CompletableFuture<Boolean> hasPermission(
			/*? if >=1.21.9 {*/ NameAndId /*?} else {*/ /*GameProfile *//*?}*/ profile,
			MinecraftServer server,
			String permission
	) {
		return ServerMultiLoader.getInstance().hasOfflinePermission(profile, server, permission);
	}

}
