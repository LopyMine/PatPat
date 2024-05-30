package net.lopymine.patpat.manager.server;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.utils.ServerNetworkUtils;

import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PatPatServerCommandManager {
	// TODO Надо будет доработать команды, хочу формат вывода потом переделать, оставлю эту тудушку тут чтобы не забыть

	private static final MutableText PATPAT_ID = Text.literal("[PatPat] ");

	public static void register() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("patpat")
				.then(literal("list")
						.then(literal("set")
								.then(argument("mode", StringArgumentType.word())
										.suggests(((context, builder) -> CommandSource.suggestMatching(List.of("whitelist", "blacklist", "disabled"), builder)))
										.executes(PatPatServerCommandManager::onSetListMode)))
						.then(literal("add")
								.then(argument("profile", GameProfileArgumentType.gameProfile())
										.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
										.executes((context) -> PatPatServerCommandManager.onListChange(context, true))))
						.then(literal("remove")
								.then(argument("profile", GameProfileArgumentType.gameProfile())
										.suggests((context, builder) -> CommandSource.suggestMatching(ServerNetworkUtils.getPlayersFromList(context.getSource().getServer().getPlayerManager(), PatPat.getConfig()), builder))
										.executes((context) -> PatPatServerCommandManager.onListChange(context, false))))
				)
		)));
	}

	private static int onListChange(CommandContext<ServerCommandSource> context, boolean add) throws CommandSyntaxException {
		PatPatServerConfig config = PatPat.getConfig();
		Map<UUID, String> players = config.getPlayers();
		Collection<GameProfile> profile = GameProfileArgumentType.getProfileArgument(context, "profile");
		for (GameProfile gameProfile : profile) {
			UUID uuid = gameProfile.getId();
			if (add) {
				if (players.put(uuid, gameProfile.getName()) == null) {
					String string = Text.stringifiedTranslatable("patpat.command.list.add.success", gameProfile.getName(), uuid).getString();
					context.getSource().sendFeedback(() -> PATPAT_ID.copy().append(string), true);
					PatPat.LOGGER.info(string);
				} else {
					String string = Text.stringifiedTranslatable("patpat.command.list.add.failed", gameProfile.getName(), uuid).getString();
					context.getSource().sendFeedback(() -> PATPAT_ID.copy().append(string), true);
					PatPat.LOGGER.warn(string);
				}
			} else {
				if (players.remove(uuid) != null) {
					String string = Text.stringifiedTranslatable("patpat.command.list.remove.success", gameProfile.getName(), uuid).getString();
					context.getSource().sendFeedback(() -> PATPAT_ID.copy().append(string), true);
					PatPat.LOGGER.info(string);
				} else {
					String string = Text.stringifiedTranslatable("patpat.command.list.remove.failed", gameProfile.getName(), uuid).getString();
					context.getSource().sendFeedback(() -> PATPAT_ID.copy().append(string), true);
					PatPat.LOGGER.warn(string);
				}
			}
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onSetListMode(CommandContext<ServerCommandSource> context) {
		String modeId = StringArgumentType.getString(context, "mode");
		ListMode listMode = ListMode.getById(modeId);
		if (listMode == null) {
			String string = Text.stringifiedTranslatable("patpat.command.list.mode.failed", modeId).getString();
			context.getSource().sendFeedback(() -> Text.of(string), true);
			PatPat.LOGGER.warn(string);
			return 0;
		}
		PatPatServerConfig config = PatPat.getConfig();
		ListMode oldMode = config.getListMode();
		config.setListMode(listMode);
		config.save();

		String string = Text.stringifiedTranslatable("patpat.command.list.mode.success", oldMode, listMode).getString();
		context.getSource().sendFeedback(() -> Text.of(string), true);
		PatPat.LOGGER.info(string);
		return Command.SINGLE_SUCCESS;
	}
}
