package net.lopymine.patpat.manager.client;

import net.minecraft.command.CommandSource;
import net.minecraft.text.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.argument.*;
import net.lopymine.patpat.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.utils.ClientNetworkUtils;

import java.util.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PatPatClientCommandManager {
	// TODO Надо будет доработать команды, хочу формат вывода потом переделать, оставлю эту тудушку тут чтобы не забыть

	private static final MutableText PATPAT_ID = Text.literal("[PatPat/Client] ");

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, environment) -> dispatcher.register(literal("patpat-client")
				.then(literal("list")
						.then(literal("set")
								.then(argument("mode", ListModeArgumentType.listMode())
										.executes(PatPatClientCommandManager::onSetListMode)))
						.then(literal("add")
								.then(argument("player", PlayerInfoArgumentType.player())
										.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
										.executes(context -> PatPatClientCommandManager.onListChange(context, true))))
						.then(literal("remove")
								.then(argument("player", PlayerInfoArgumentType.player())
										.suggests((context, builder) -> CommandSource.suggestMatching(ClientNetworkUtils.getOnlinePlayersFromUuids(context.getSource().getClient().getNetworkHandler(), PatPatClient.getConfig()), builder))
										.executes(context -> PatPatClientCommandManager.onListChange(context, false))))
				)
		)));
	}

	private static int onListChange(CommandContext<FabricClientCommandSource> context, boolean add) {
		PatPatClientConfig config = PatPatClient.getConfig();
		Map<UUID, String> players = config.getPlayers();
		PlayerInfo playerInfo = PlayerInfoArgumentType.getPlayerInfo("player", context);

		UUID uuid = playerInfo.getUuid();
		if (add) {
			if (players.put(uuid, playerInfo.getNickname()) == null) {
				String string = Text.stringifiedTranslatable("patpat.command.list.add.success", playerInfo.getNickname(), playerInfo.getUuid()).getString();
				context.getSource().sendFeedback(PATPAT_ID.copy().append(string));
				PatPat.LOGGER.info(string);
			} else {
				String string = Text.stringifiedTranslatable("patpat.command.list.add.failed", playerInfo.getNickname(), playerInfo.getUuid()).getString();
				context.getSource().sendFeedback(PATPAT_ID.copy().append(string));
				PatPat.LOGGER.warn(string);
			}
		} else {
			if (players.remove(uuid) != null) {
				String string = Text.stringifiedTranslatable("patpat.command.list.remove.success", playerInfo.getNickname(), playerInfo.getUuid()).getString();
				context.getSource().sendFeedback(PATPAT_ID.copy().append(string));
				PatPat.LOGGER.info(string);
			} else {
				String string = Text.stringifiedTranslatable("patpat.command.list.remove.failed", playerInfo.getNickname(), playerInfo.getUuid()).getString();
				context.getSource().sendFeedback(PATPAT_ID.copy().append(string));
				PatPat.LOGGER.warn(string);
			}
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onSetListMode(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClient.getConfig();
		ListMode oldMode = config.getListMode();
		ListMode mode = ListModeArgumentType.getListMode(context, "mode");
		config.setListMode(mode);
		config.save();

		String string = Text.stringifiedTranslatable("patpat.command.list.mode.success", oldMode.asString(), mode.asString()).getString();
		context.getSource().sendFeedback(Text.of(string));
		PatPat.LOGGER.info(string);
		return Command.SINGLE_SUCCESS;
	}
}
