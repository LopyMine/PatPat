package net.lopymine.patpat.manager.client;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.text.*;
import net.minecraft.text.ClickEvent.Action;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.*;

import net.lopymine.patpat.argument.*;
import net.lopymine.patpat.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.extension.TextExtension;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@ExtensionMethod(TextExtension.class)
public class PatPatClientCommandManager {
	private static final MutableText PATPAT_ID = Text.literal("[§aPatPat/Client§f] ");

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
		String name = playerInfo.getNickname();

		boolean success = false;
		if (add) {
			if (players.put(uuid, name) == null) {
				success = true;
			}
		} else {
			if (players.remove(uuid) != null) {
				success = true;
			}
		}

		String action = add ? "add" : "remove";
		String result = success ? "success" : "failed";
		String key = String.format("patpat.command.list.%s.%s", action, result);

		Text text = CommandTextBuilder.startBuilder(key, name)
				.withShowEntity(EntityType.PLAYER, uuid, name)
				.withClickEvent(Action.COPY_TO_CLIPBOARD, uuid)
				.build();

		context.getSource().sendFeedback(PATPAT_ID.copy().append(text));
		if (success) {
			PatPatClient.LOGGER.info(text.asString());
		} else {
			PatPatClient.LOGGER.warn(text.asString());
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onSetListMode(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClient.getConfig();
		ListMode mode = ListModeArgumentType.getListMode(context, "mode");
		config.setListMode(mode);
		config.save();

		Text text = CommandTextBuilder.startBuilder("patpat.command.list.mode.success", mode.getText()).build();

		context.getSource().sendFeedback(PATPAT_ID.copy().append(text));
		PatPatClient.LOGGER.info(text.asString());
		return Command.SINGLE_SUCCESS;
	}
}
