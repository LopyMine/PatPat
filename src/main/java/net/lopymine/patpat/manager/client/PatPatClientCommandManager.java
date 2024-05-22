package net.lopymine.patpat.manager.client;

import net.minecraft.command.CommandSource;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.argument.PlayerInfoArgumentType;
import net.lopymine.patpat.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.ListConfig;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.utils.ClientNetworkUtils;

import java.util.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PatPatClientCommandManager {
	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, environment) -> dispatcher.register(literal("patpat-client")
				.then(literal("whitelist")
						.then(literal("enable").executes((context) -> PatPatClientCommandManager.onListEnable(context, true, true)))
						.then(literal("disable").executes((context) -> PatPatClientCommandManager.onListEnable(context, true, false)))
						.then(literal("add")
								.then(argument("player", PlayerInfoArgumentType.player())
										.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
										.executes((context) -> PatPatClientCommandManager.onListChange(context, true, true))))
						.then(literal("remove")
								.then(argument("player", PlayerInfoArgumentType.player())
										.suggests((context, builder) -> CommandSource.suggestMatching(ClientNetworkUtils.getOnlinePlayersFromUuids(context.getSource().getClient().getNetworkHandler(), PatPatClient.getConfig().getWhitelist()), builder))
										.executes((context) -> PatPatClientCommandManager.onListChange(context, true, false))))
				)
				.then(literal("blacklist")
						.then(literal("enable").executes((context) -> PatPatClientCommandManager.onListEnable(context, false, true)))
						.then(literal("disable").executes((context) -> PatPatClientCommandManager.onListEnable(context, false, false)))
						.then(literal("add")
								.then(argument("player", PlayerInfoArgumentType.player())
										.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
										.executes((context) -> PatPatClientCommandManager.onListChange(context, false, true))))
						.then(literal("remove")
								.then(argument("player", PlayerInfoArgumentType.player())
										.suggests((context, builder) -> CommandSource.suggestMatching(ClientNetworkUtils.getOnlinePlayersFromUuids(context.getSource().getClient().getNetworkHandler(), PatPatClient.getConfig().getBlacklist()), builder))
										.executes((context) -> PatPatClientCommandManager.onListChange(context, false, true))))
				)
		)));
	}

	private static int onListChange(CommandContext<FabricClientCommandSource> context, boolean whitelist, boolean add) {
		PatPatClientConfig config = PatPatClient.getConfig();
		ListConfig listConfig = whitelist ? config.getWhitelist() : config.getBlacklist();
		Map<UUID, String> players = listConfig.getPlayers();
		PlayerInfo playerInfo = PlayerInfoArgumentType.getPlayerInfo("player", context);

		UUID uuid = playerInfo.getUuid();
		String wtf = whitelist ? "WHITELIST" : "BLACKLIST";
		if (add) {
			if (players.put(uuid, playerInfo.getNickname()) == null) {
				PatPat.LOGGER.info("Successfully added to {} player[{}, {}]", wtf, playerInfo.getNickname(), playerInfo.getUuid());
			} else {
				PatPat.LOGGER.warn("Player[{}, {}] already added to {}", playerInfo.getNickname(), playerInfo.getUuid(), wtf);
			}
		} else {
			if (players.remove(uuid) != null) {
				PatPat.LOGGER.info("Successfully removed from {} player[{}, {}]", wtf, playerInfo.getNickname(), playerInfo.getUuid());
			} else {
				PatPat.LOGGER.warn("Player[{}, {}] already not found in {}", playerInfo.getNickname(), playerInfo.getUuid(), wtf);
			}
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onListEnable(CommandContext<FabricClientCommandSource> ignored, boolean whitelist, boolean enable) {
		PatPatClientConfig config = PatPatClient.getConfig();
		ListConfig listConfig = whitelist ? config.getWhitelist() : config.getBlacklist();
		listConfig.setEnable(enable);
		config.save();
		return Command.SINGLE_SUCCESS;
	}
}
