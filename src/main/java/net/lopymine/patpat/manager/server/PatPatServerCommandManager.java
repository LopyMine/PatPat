package net.lopymine.patpat.manager.server;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.extension.TextExtension;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//? >=1.19 {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//?} else {
/*import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
*///?}

@ExtensionMethod(TextExtension.class)
public class PatPatServerCommandManager {

	private static final MutableText PATPAT_ID = TextUtils.literal("[§aPatPat§f] ");

	private PatPatServerCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((/*? >=1.19 {*/(dispatcher, registryAccess, environment)/*?} else {*//*(dispatcher, dedicated)*//*?}*/ -> {
			//? <=1.18.2 {
			/*if (!dedicated) {
				return;
			}
			*///?}
			dispatcher.register(literal("patpat")
					.requires(context -> context.hasPermissionLevel(2))
					.then(literal("list")
							.then(literal("set")
									.then(argument("mode", StringArgumentType.word())
											.suggests(((context, builder) -> CommandSource.suggestMatching(List.of("WHITELIST", "BLACKLIST", "DISABLED"), builder)))
											.executes(PatPatServerCommandManager::onSetListMode)))
							.then(literal("add")
									.then(argument("profile", GameProfileArgumentType.gameProfile())
											.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
											.executes(context -> PatPatServerCommandManager.onListChange(context, true))))
							.then(literal("remove")
									.then(argument("profile", GameProfileArgumentType.gameProfile())
											.suggests((context, builder) -> CommandSource.suggestMatching(ServerNetworkUtils.getPlayersFromList(context.getSource()./*? <=1.17 {*//*getMinecraftServer()*//*?} else {*/getServer()/*?}*/.getPlayerManager(), PatPat.getConfig()), builder))
											.executes(context -> PatPatServerCommandManager.onListChange(context, false))))
					)
			);
		}));
	}

	private static int onListChange(CommandContext<ServerCommandSource> context, boolean add) throws CommandSyntaxException {
		PatPatServerConfig config = PatPat.getConfig();
		Map<UUID, String> players = config.getList();
		Collection<GameProfile> profile = GameProfileArgumentType.getProfileArgument(context, "profile");
		for (GameProfile gameProfile : profile) {
			String name = gameProfile.getName();
			UUID uuid = gameProfile.getId();

			boolean success = add ? !players.containsKey(uuid) && players.put(uuid, name) == null : players.containsKey(uuid) && players.remove(uuid) != null;

			String action = add ? "add" : "remove";
			String result = success ? "success" : "failed";
			String key = String.format("list.%s.%s", action, result);

			Text text = CommandTextBuilder.startBuilder(key, name)
					.withShowEntity(EntityType.PLAYER, uuid, name)
					.withCopyToClipboard(uuid)
					.build();

			context.getSource().sendFeedback(/*? >=1.20 {*/() -> PATPAT_ID.copy().append(text)/*?} else {*//*PATPAT_ID.copy().append(text)*//*?}*/, true);
			if (success) {
				PatPat.LOGGER.info(text.asString());
			} else {
				PatPat.LOGGER.warn(text.asString());
			}
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

	private static int onSetListMode(CommandContext<ServerCommandSource> context) {
		String modeId = StringArgumentType.getString(context, "mode");
		PatPatServerConfig config = PatPat.getConfig();
		ListMode listMode = ListMode.getById(modeId);

		boolean success = listMode != null;
		if (success) {
			config.setListMode(listMode);
		}

		String result = success ? "success" : "failed";
		String key = String.format("list.mode.%s", result);
		Object arg = success ? listMode.getText() : modeId;

		CommandTextBuilder builder = CommandTextBuilder.startBuilder(key, arg);
		if (!success) {
			builder.withHoverText(Arrays.stream(ListMode.values()).map(ListMode::getText).toArray());
		}
		Text text = builder.build();
		context.getSource().sendFeedback(/*? >=1.20 {*/() -> PATPAT_ID.copy().append(text)/*?} else {*//*PATPAT_ID.copy().append(text)*//*?}*/, true);
		if (success) {
			PatPat.LOGGER.info(text.asString());
		} else {
			PatPat.LOGGER.warn(text.asString());
		}
		return success ? Command.SINGLE_SUCCESS : 0;
	}
}
