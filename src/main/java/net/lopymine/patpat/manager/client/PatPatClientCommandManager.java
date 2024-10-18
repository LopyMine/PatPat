package net.lopymine.patpat.manager.client;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.argument.*;
import net.lopymine.patpat.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.extension.TextExtension;
import net.lopymine.patpat.utils.*;

import java.util.*;

//? >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//?} else {
/*import net.fabricmc.fabric.api.client.command.v1.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
*///?}

@ExtensionMethod(TextExtension.class)
public class PatPatClientCommandManager {

	private static final MutableText PATPAT_ID = TextUtils.literal("[§aPatPat/Client§f] ");
	private static final String PLAYER_ARGUMENT_NAME = "player";

	private PatPatClientCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		/*? >=1.19 {*/
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, environment) -> dispatcher
				/*?} else {*/
				/*ClientCommandManager.DISPATCHER
				 *//*?}*/
				.register(literal("patpat-client")
						.then(literal("list")
								.then(literal("set")
										.then(argument("mode", ListModeArgumentType.listMode())
												.executes(PatPatClientCommandManager::onSetListMode)))
								.then(literal("add")
										.then(argument(PLAYER_ARGUMENT_NAME, PlayerInfoArgumentType.player())
												.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
												.executes(context -> PatPatClientCommandManager.onListChange(context, true))))
								.then(literal("remove")
										.then(argument(PLAYER_ARGUMENT_NAME, PlayerInfoArgumentType.player())
												.suggests((context, builder) -> CommandSource.suggestMatching(ClientNetworkUtils.getOnlinePlayersFromUuids(context.getSource().getClient().getNetworkHandler(), PatPatClient.getConfig()), builder))
												.executes(context -> PatPatClientCommandManager.onListChange(context, false))))
						)
				)
				/*? >=1.19 {*/))/*?}*/;
	}

	private static int onListChange(CommandContext<FabricClientCommandSource> context, boolean add) {
		PatPatClientConfig config = PatPatClient.getConfig();
		Map<UUID, String> players = config.getPlayers();
		PlayerInfo playerInfo = PlayerInfoArgumentType.getPlayerInfo(PLAYER_ARGUMENT_NAME, context);
		UUID uuid = playerInfo.getUuid();
		String name = playerInfo.getNickname();

		boolean success = add ? !players.containsKey(uuid) && players.put(uuid, name) == null : players.containsKey(uuid) && players.remove(uuid) != null;

		String action = add ? "add" : "remove";
		String result = success ? "success" : "failed";
		String key = String.format("list.%s.%s", action, result);

		Text text = CommandTextBuilder.startBuilder(key, name)
				.withShowEntity(EntityType.PLAYER, uuid, name)
				.withClickEvent(Action.COPY_TO_CLIPBOARD, uuid)
				.build();

		context.getSource().sendFeedback(PATPAT_ID.copy().append(text));
		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onSetListMode(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClient.getConfig();
		ListMode mode = ListModeArgumentType.getListMode(context, "mode");
		config.setListMode(mode);
		config.save();

		Text text = CommandTextBuilder.startBuilder("list.mode.success", mode.getText()).build();

		context.getSource().sendFeedback(PATPAT_ID.copy().append(text));
		return Command.SINGLE_SUCCESS;
	}
}
