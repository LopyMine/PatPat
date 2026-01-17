//~ client_fabric_commands

package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.client.command.argument.*;
import net.lopymine.patpat.client.command.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.config.list.PatPatClientPlayerListConfig;
import net.lopymine.patpat.common.command.list.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.commands.*;

import java.util.*;

import net.minecraft.commands.CommandSourceStack;
import static net.lopymine.patpat.common.command.PatPatCommonCommandHelper.literal;
import static net.lopymine.patpat.common.command.PatPatCommonCommandHelper.argument;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListChangeCommand {

	private static final String PLAYER_ARGUMENT_NAME = "player";

	private PatPatClientListChangeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> getAdd() {
		return literal("add")
				.then(argument(PLAYER_ARGUMENT_NAME, PlayerInfoArgumentType.player())
						.suggests(((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames(), builder)))
						.executes(context -> onListChange(context, true)));
	}

	public static LiteralArgumentBuilder<CommandSourceStack> getRemove() {
		return literal("remove")
				.then(argument(PLAYER_ARGUMENT_NAME, PlayerInfoArgumentType.player())
						.suggests((context, builder) -> SharedSuggestionProvider.suggest(ClientNetworkUtils.getOnlinePlayersFromUuids(), builder))
						.executes(context -> onListChange(context, false)));
	}

	private static int onListChange(CommandContext<CommandSourceStack> context, boolean add) {
		PatPatClientPlayerListConfig config = PatPatClientPlayerListConfig.getInstance();
		Map<UUID, String> map = config.getValues();
		PlayerInfo playerInfo = PlayerInfoArgumentType.getPlayerInfo(PLAYER_ARGUMENT_NAME, context);
		UUID uuid = playerInfo.uuid();
		String name = playerInfo.nickname();

		PatPatCommonListChangeCommand.changeList(add, map, uuid, name, component -> context.sendMsg(component));

		config.saveAsync();
		return Command.SINGLE_SUCCESS;
	}

}
