package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.common.config.time.Time;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.utils.*;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.lopymine.patpat.server.command.ratelimit.set.PatPatServerRateLimitSetCommand.VALUE_KEY;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitIntervalCommand {

	private static final Object TIME_EXAMPLES_ARG = CommandText.getColoredArg(ChatFormatting.GRAY, "1sec, 5s, 5min");

	private PatPatServerRateLimitIntervalCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("interval")
				.requires(context -> context.hasPatPatPermission("ratelimit.set.interval"))
				.then(argument(VALUE_KEY, StringArgumentType.word())
						.executes(PatPatServerRateLimitIntervalCommand::set));
	}

	public static int set(CommandContext<CommandSourceStack> context) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		String value = StringArgumentType.getString(context, "value");
		try {
			Time time = Time.of(value);
			if (time.getValue() < 1) {
				Component text = CommandText.goldenArgs("error.time_less_than", time, "1sec").finish();
				context.sendMsg(text);
				return 0;
			}
			rateLimitConfig.setTokenIncrementInterval(time);
			config.saveAsync();
			PatPatServerRateLimitManager.reloadTask();
			Component text = CommandText.goldenArgs("ratelimit.set.interval", time).finish();
			context.sendMsg(text);
		} catch (IllegalArgumentException ignored) {
			Component text = CommandText.text("error.not_time", CommandText.getColoredArg(ChatFormatting.GOLD, value), TIME_EXAMPLES_ARG).finish();
			context.sendMsg(text);
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}

}
