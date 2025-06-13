package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.client.command.PatPatClientCommandManager;
import net.lopymine.patpat.client.command.argument.*;
import net.lopymine.patpat.client.command.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.config.sub.PatPatClientPlayerListConfig;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
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
		Map<UUID, String> players = config.getMap();
		PlayerInfo playerInfo = PlayerInfoArgumentType.getPlayerInfo(PLAYER_ARGUMENT_NAME, context);
		UUID uuid = playerInfo.getUuid();
		String name = playerInfo.getNickname();

		boolean success = add ? !players.containsKey(uuid) && players.put(uuid, name) == null : players.containsKey(uuid) && players.remove(uuid) != null;

		String action = add ? "add" : "remove";
		String result = success ? "success" : "already";
		String key = String.format("list.%s.%s", action, result);

		Component text = CommandTextBuilder.startBuilder(key, TextUtils.literal(name).withStyle(ChatFormatting.GOLD))
				.withShowEntity(EntityType.PLAYER, uuid, name)
				.withCopyToClipboard(uuid)
				.build();

		context.getSource().sendPatPatFeedback(text);
		config.saveAsync();
		return Command.SINGLE_SUCCESS;
	}

}
