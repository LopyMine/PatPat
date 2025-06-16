package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.client.command.argument.*;
import net.lopymine.patpat.client.command.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.config.PatPatClientPlayerListConfig;
import net.lopymine.patpat.common.command.list.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.commands.SharedSuggestionProvider;

import java.util.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListChangeCommand {

	private static final String PLAYER_ARGUMENT_NAME = "player";

	private PatPatClientListChangeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> getAdd() {
		return literal("add")
						.then(argument(PLAYER_ARGUMENT_NAME, PlayerInfoArgumentType.player())
								.suggests(((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames(), builder)))
								.executes(context -> onListChange(context, true)));
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> getRemove() {
		return literal("remove")
						.then(argument(PLAYER_ARGUMENT_NAME, PlayerInfoArgumentType.player())
								.suggests((context, builder) -> SharedSuggestionProvider.suggest(ClientNetworkUtils.getOnlinePlayersFromUuids(context.getSource().getClient().getConnection()), builder))
								.executes(context -> onListChange(context, false)));
	}

	private static int onListChange(CommandContext<FabricClientCommandSource> context, boolean add) {
		PatPatClientPlayerListConfig config = PatPatClientPlayerListConfig.getInstance();
		Map<UUID, String> map = config.getMap();
		PlayerInfo playerInfo = PlayerInfoArgumentType.getPlayerInfo(PLAYER_ARGUMENT_NAME, context);
		UUID uuid = playerInfo.getUuid();
		String name = playerInfo.getNickname();

		PatPatCommonListChangeCommand.changeList(add, map, uuid, name, (component) -> context.sendMsg(component));
		
		config.saveAsync();
		return Command.SINGLE_SUCCESS;
	}

}
