package net.lopymine.patpat.manager.server;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.*;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//? >=1.19 {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//?} else {
/*import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
 *///?}


@ExtensionMethod({TextExtension.class, CommandExtenstion.class})
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
					.then(literal("reload").executes(context -> {
						PatPatConfigManager.reload();
						Text text = CommandTextBuilder.startBuilder("reload.success").build();
						context.getSource().sendPatPatFeedback(PATPAT_ID.copy().append(text), false);
						PatPat.LOGGER.info(text.asString());
						return Command.SINGLE_SUCCESS;
					}))


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
											.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
											.executes(context -> PatPatServerCommandManager.onListChange(context, false))))
					)

//(enable | disable | set | info)
					.then(literal("ratelimit")
							.then(literal("enable"))
							.then(literal("disable"))
							.then(literal("info")
									.then(argument("profile", GameProfileArgumentType.gameProfile())
											.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
											.executes(context -> PatPatServerCommandManager.onListChange(context, false)))

							)
							.then(literal("set"))


					)
			);
		}));
	}

	private static int onListChange(CommandContext<ServerCommandSource> context, boolean add) throws CommandSyntaxException {
		PlayerListConfig config = PlayerListConfig.getInstance();
		Set<UUID> uuids = config.getUuids();
		Collection<GameProfile> profile = GameProfileArgumentType.getProfileArgument(context, "profile");
		String action = add ? "add" : "remove";
		boolean modify = false;
		for (GameProfile gameProfile : profile) {
			UUID uuid = gameProfile.getId();
			boolean success = false;
			if (add && !uuids.contains(uuid)) {
				uuids.add(uuid);
				success = true;
				modify  = true;
			} else if (!add && uuids.contains(uuid)) {
				uuids.remove(uuid);
				success = true;
				modify  = true;
			}

			String result = success ? "success" : "failed";
			String key = String.format("list.%s.%s", action, result);
			Text text = CommandTextBuilder.startBuilder(key, gameProfile.getName())
					.withShowEntity(EntityType.PLAYER, uuid, gameProfile.getName())
					.withClickEvent(Action.COPY_TO_CLIPBOARD, uuid)
					.build();

			context.getSource().sendPatPatFeedback(PATPAT_ID.copy().append(text), true);
			if (success) {
				PatPat.LOGGER.info(text.asString());
			} else {
				PatPat.LOGGER.warn(text.asString());
			}
		}
		if (modify) {
			config.save();
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int onSetListMode(CommandContext<ServerCommandSource> context) {
		String modeId = StringArgumentType.getString(context, "mode");
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		ListMode listMode = ListMode.getById(modeId);
		if (config.getListMode() == listMode) {
			Text text = CommandTextBuilder.startBuilder("list.mode.already", listMode).build();
			context.getSource().sendPatPatFeedback(PATPAT_ID.copy().append(text), true);
			return 0;
		}

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
		context.getSource().sendPatPatFeedback(PATPAT_ID.copy().append(text), true);
		if (success) {
			PatPat.LOGGER.info(text.asString());
		} else {
			PatPat.LOGGER.warn(text.asString());
		}
		return success ? Command.SINGLE_SUCCESS : 0;
	}
}
