package net.lopymine.patpat.extension;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;

import java.util.concurrent.CompletableFuture;

public class PlayerExtension {

	private PlayerExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static boolean hasPermission(ServerPlayer player, String permission) {
		return hasPermission(player, permission, 2);
	}

	public static boolean hasPermission(ServerPlayer player, String permission, int defaultLevel) {
		//? <1.17.1 {
		/*return player./^? if >=1.17.1 {^/hasPermissionLevel/^?} else {^//^hasPermissions^//^?}^/(defaultLevel);
		*///?} else {
		return Permissions.check(player, permission, defaultLevel);
		 //?}
	}

	public static CompletableFuture<Boolean> hasPermission(GameProfile profile, String permission, CommandContext<CommandSourceStack> context) {
		return hasPermission(profile, permission, 2, context);
	}

	public static CompletableFuture<Boolean> hasPermission(GameProfile profile, String permission, int defaultLevel, CommandContext<CommandSourceStack> context) {
		//? <1.17.1 {
		/*return CompletableFuture.completedFuture(context.getSource()./^? if >=1.17.1 {^/getMinecraftServer().getPermissionLevel(profile)/^?} else {^/ /^getServer().getProfilePermissions(profile) ^//^?}^/ >= defaultLevel);
		*///?} else {
		MinecraftServer server = context.getSource().getServer();
		return Permissions.check(profile, permission, defaultLevel, server);
		//?}
	}
}
