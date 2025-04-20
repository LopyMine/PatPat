package net.lopymine.patpat.server.command;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.text.MutableText;

import com.mojang.brigadier.arguments.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.command.list.*;
import net.lopymine.patpat.server.command.ratelimit.*;
import net.lopymine.patpat.server.command.ratelimit.set.*;
import net.lopymine.patpat.server.command.reload.PatPatServerConfigReloadCommand;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.utils.TextUtils;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//? >=1.19 {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//?} else {
/*import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
 *///?}


@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerCommandManager {

	public static final MutableText PATPAT_ID = TextUtils.literal("[§aPatPat§f] ");

	public static final String PROFILE_NAME = "profile";
	public static final String VALUE_NAME = "value";

	private PatPatServerCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> PatPatServerRateLimitManager.runTask());
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> PatPatServerRateLimitManager.stopTask());

		// TODO: В идеале разделить это на несколько классов/методов
		CommandRegistrationCallback.EVENT.register((/*? >=1.19 {*/(dispatcher, registryAccess, environment)/*?} else {*//*(dispatcher, dedicated)*//*?}*/ -> {
			//? <=1.18.2 {
			/*if (!dedicated) {
				return;
			}
			*///?}
			dispatcher.register(literal("patpat")
					.then(literal("version")
							.requires(context -> context.hasPatPatPermission("version"))
							.executes(PatPatServerRateLimitVersionCommand::version))
					.then(literal("reload")
							.requires(context -> context.hasPatPatPermission("reload"))
							.executes(PatPatServerConfigReloadCommand::reload))
					.then(literal("list")
							.then(literal("set")
									.requires(context -> context.hasPatPatPermission("list.set"))
									.then(argument("mode", StringArgumentType.word())
											.suggests(((context, builder) -> CommandSource.suggestMatching(List.of("WHITELIST", "BLACKLIST", "DISABLED"), builder)))
											.executes(PatPatServerListSetModeCommand::setListMode)))
							.then(literal("add")
									.requires(context -> context.hasPatPatPermission("list.add"))
									.then(argument(PROFILE_NAME, GameProfileArgumentType.gameProfile())
											.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
											.executes(PatPatServerListCommand::add)))
							.then(literal("remove")
									.requires(context -> context.hasPatPatPermission("list.remove"))
									.then(argument(PROFILE_NAME, GameProfileArgumentType.gameProfile())
											.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
											.executes(PatPatServerListCommand::remove)))
					)

					.then(literal("ratelimit")
							.then(literal("enable")
									.requires(context -> context.hasPatPatPermission("ratelimit.toggle"))
									.executes(PatPatServerRateLimitToggleCommand::enable)
							)
							.then(literal("disable")
									.requires(context -> context.hasPatPatPermission("ratelimit.toggle"))
									.executes(PatPatServerRateLimitToggleCommand::disable)
							)
							.then(literal("info")
									.requires(context -> context.hasPatPatPermission("ratelimit.info"))
									.executes(PatPatServerRateLimitInfoCommand::info)
									.then(argument(PROFILE_NAME, GameProfileArgumentType.gameProfile())
											.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
											.executes(PatPatServerRateLimitInfoCommand::infoWithUser)
									)
							)
							.then(literal("set")
									.then(literal("interval")
											.requires(context -> context.hasPatPatPermission("ratelimit.set.interval"))
											.executes(PatPatServerRateLimitIntervalCommand::info)
											.then(argument(VALUE_NAME, StringArgumentType.word())
													.executes(PatPatServerRateLimitIntervalCommand::set)
											)
									)
									.then(literal("increment")
											.requires(context -> context.hasPatPatPermission("ratelimit.set.increment"))
											.executes(PatPatServerRateLimitIncrementCommand::info)
											.then(argument(VALUE_NAME, IntegerArgumentType.integer(1))
													.executes(PatPatServerRateLimitIncrementCommand::set)
											)
									)
									.then(literal("limit")
											.requires(context -> context.hasPatPatPermission("ratelimit.set.limit"))
											.executes(PatPatServerRateLimitLimitCommand::info)
											.then(argument(VALUE_NAME, IntegerArgumentType.integer(1))
													.executes(PatPatServerRateLimitLimitCommand::set)
											)
									)
							)
					)
			);
		}));
	}

	public static String getPermission(String permission) {
		return "%s.%s".formatted(PatPat.MOD_ID, permission);
	}
}
