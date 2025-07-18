package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.utils.*;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.lopymine.patpat.server.command.ratelimit.set.PatPatServerRateLimitSetCommand.VALUE_KEY;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitLimitCommand {

	private PatPatServerRateLimitLimitCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("limit")
				.requires(context -> context.hasPatPatPermission("ratelimit.set.limit"))
				.then(argument(VALUE_KEY, IntegerArgumentType.integer(1))
						.executes(PatPatServerRateLimitLimitCommand::set));
	}

	public static int set(CommandContext<CommandSourceStack> context) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		int value = IntegerArgumentType.getInteger(context, "value");
		rateLimitConfig.setTokenLimit(value);
		config.saveAsync();
		Component text = CommandText.goldenArgs(
				"ratelimit.set.limit",
				value
		).finish();
		context.sendMsg(text);
		return Command.SINGLE_SUCCESS;
	}

}
