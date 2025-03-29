package net.lopymine.patpat.manager.server;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.text.*;

import com.mojang.brigadier.arguments.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.manager.server.command.ConfigReloadCommand;
import net.lopymine.patpat.manager.server.command.list.*;
import net.lopymine.patpat.manager.server.command.ratelimit.*;
import net.lopymine.patpat.manager.server.command.ratelimit.set.*;
import net.lopymine.patpat.utils.*;

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
		// TODO: В идеале разделить это на несколько классов/методов
		CommandRegistrationCallback.EVENT.register((/*? >=1.19 {*/(dispatcher, registryAccess, environment)/*?} else {*//*(dispatcher, dedicated)*//*?}*/ -> {
			//? <=1.18.2 {
			/*if (!dedicated) {
				return;
			}
			*///?}
			dispatcher.register(literal("patpat")
					.then(literal("reload")
							.requires(context -> context.hasPermission(getPermission("reload")))
							.executes(ConfigReloadCommand::reload))
					.then(literal("list")
							.then(literal("set")
									.requires(context -> context.hasPermission(getPermission("list.set")))
									.then(argument("mode", StringArgumentType.word())
											.suggests(((context, builder) -> CommandSource.suggestMatching(List.of("WHITELIST", "BLACKLIST", "DISABLED"), builder)))
											.executes(ListSetModeCommand::setListMode)))
							.then(literal("add")
									.requires(context -> context.hasPermission(getPermission("list.add")))
									.then(argument(PROFILE_NAME, GameProfileArgumentType.gameProfile())
											.suggests(((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder)))
											.executes(ListCommand::add)))
							.then(literal("remove")
									.requires(context -> context.hasPermission(getPermission("list.remove")))
									.then(argument(PROFILE_NAME, GameProfileArgumentType.gameProfile())
											.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
											.executes(ListCommand::remove)))
					)

					.then(literal("ratelimit")
							.then(literal("enable")
									.requires(context -> context.hasPermission(getPermission("ratelimit.toggle")))
									.executes(ToggleCommand::enable)
							)
							.then(literal("disable")
									.requires(context -> context.hasPermission(getPermission("ratelimit.toggle")))
									.executes(ToggleCommand::disable)
							)
							.then(literal("info")
									.requires(context -> context.hasPermission(getPermission("ratelimit.info")))
									.executes(InfoCommand::info)
									.then(argument(PROFILE_NAME, GameProfileArgumentType.gameProfile())
											.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
											.executes(InfoCommand::infoWithUser)
									)

							)
							.then(literal("set")
									.requires(context -> context.hasPermission(getPermission("ratelimit.set")))
									.then(literal("interval")
											.requires(context -> context.hasPermission(getPermission("ratelimit.set.interval")))
											.executes(IntervalCommand::info)
											.then(argument(VALUE_NAME, StringArgumentType.word())
													.executes(IntervalCommand::set)
											)
									)
									.then(literal("increment")
											.requires(context -> context.hasPermission(getPermission("ratelimit.set.increment")))
											.executes(IncrementCommand::info)
											.then(argument(VALUE_NAME, IntegerArgumentType.integer(1))
													.executes(IncrementCommand::set)
											)
									)
									.then(literal("limit")
											.requires(context -> context.hasPermission(getPermission("ratelimit.set.limit")))
											.executes(LimitCommand::info)
											.then(argument(VALUE_NAME, IntegerArgumentType.integer(1))
													.executes(LimitCommand::set)
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
