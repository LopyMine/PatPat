package net.lopymine.patpat.extension;

import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.authlib.GameProfile;

import java.util.concurrent.CompletableFuture;

/*? if >=1.21.9*/
//import net.minecraft.server.players.NameAndId;

public class PlayerExtension {

	private PlayerExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static boolean hasPermission(ServerPlayer player, String permission) {
		return ServerMultiLoader.getInstance().hasPermission(player, permission);
	}

	public static CompletableFuture<Boolean> hasPermission(GameProfile profile, String permission) {
		return ServerMultiLoader.getInstance().hasOfflinePermission(profile.getId(), permission);
	}

	/*? if >=1.21.9 {*/
	/*public static CompletableFuture<Boolean> hasPermission(NameAndId nameAndId, String permission) {
		return ServerMultiLoader.getInstance().hasOfflinePermission(profile.getId(), permission);
	}
	*//*?}*/
}
