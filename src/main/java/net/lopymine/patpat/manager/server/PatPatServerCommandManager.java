package net.lopymine.patpat.manager.server;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.ListConfig;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.utils.ServerNetworkUtils;

import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PatPatServerCommandManager {
	public static void register() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("patpat-server")
				.then(literal("whitelist")
						.then(literal("enable").executes((context) -> PatPatServerCommandManager.onListEnable(context, true, true)))
						.then(literal("disable").executes((context) -> PatPatServerCommandManager.onListEnable(context, true, false)))
						.then(literal("add")
								.then(argument("profile", GameProfileArgumentType.gameProfile())
										.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
										.executes((context) -> PatPatServerCommandManager.onListChange(context, true, true))))
						.then(literal("remove")
								.then(argument("profile", GameProfileArgumentType.gameProfile())
										.suggests((context, builder) -> CommandSource.suggestMatching(ServerNetworkUtils.getPlayersFromList(context.getSource().getServer().getPlayerManager(), PatPat.getConfig().getWhitelist()), builder))
										.executes((context) -> PatPatServerCommandManager.onListChange(context, true, false))))
				)
				.then(literal("blacklist")
						.then(literal("enable").executes((context) -> PatPatServerCommandManager.onListEnable(context, false, true)))
						.then(literal("disable").executes((context) -> PatPatServerCommandManager.onListEnable(context, false, false)))
						.then(literal("add")
								.then(argument("profile", GameProfileArgumentType.gameProfile())
										.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
										.executes((context) -> PatPatServerCommandManager.onListChange(context, false, true))))
						.then(literal("remove")
								.then(argument("profile", GameProfileArgumentType.gameProfile())
										.suggests((context, builder) -> CommandSource.suggestMatching(ServerNetworkUtils.getPlayersFromList(context.getSource().getServer().getPlayerManager(), PatPat.getConfig().getBlacklist()), builder))
										.executes((context) -> PatPatServerCommandManager.onListChange(context, false, true))))
				)
		)));
	}

	private static int onListChange(CommandContext<ServerCommandSource> context, boolean whitelist, boolean add) throws CommandSyntaxException {
		PatPatServerConfig config = PatPat.getConfig();
		ListConfig listConfig = whitelist ? config.getWhitelist() : config.getBlacklist();
		Map<UUID, String> players = listConfig.getPlayers();
		Collection<GameProfile> profile = GameProfileArgumentType.getProfileArgument(context, "profile");
		for (GameProfile gameProfile : profile) {
			UUID uuid = gameProfile.getId();
			String wtf = whitelist ? "WHITELIST" : "BLACKLIST";
			if (add) {
				if (players.put(uuid, gameProfile.getName()) == null) {
					PatPat.LOGGER.info("Successfully added to {} player[{}, {}]", wtf, gameProfile.getName(), gameProfile.getId());
				} else {
					PatPat.LOGGER.warn("Player[{}, {}] already added to {}", gameProfile.getName(), gameProfile.getId(), wtf);
				}
			} else {
				if (players.remove(uuid) != null) {
					PatPat.LOGGER.info("Successfully removed from {} player[{}, {}]", wtf, gameProfile.getName(), gameProfile.getId());
				} else {
					PatPat.LOGGER.warn("Player[{}, {}] already removed from {}", gameProfile.getName(), gameProfile.getId(), wtf);
				}
			}
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onListEnable(CommandContext<ServerCommandSource> ignored, boolean whitelist, boolean enable) {
		PatPatServerConfig config = PatPat.getConfig();
		ListConfig listConfig = whitelist ? config.getWhitelist() : config.getBlacklist();
		listConfig.setEnable(enable);

		config.save();
		return Command.SINGLE_SUCCESS;
	}
}
