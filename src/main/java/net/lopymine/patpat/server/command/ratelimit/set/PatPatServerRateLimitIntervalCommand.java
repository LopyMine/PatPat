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

	private PatPatServerRateLimitIntervalCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("interval")
				.requires(context -> context.hasPatPatPermission("ratelimit.set.interval"))
				.executes(PatPatServerRateLimitIntervalCommand::info)
				.then(argument(VALUE_KEY, StringArgumentType.word())
						.executes(PatPatServerRateLimitIntervalCommand::set)
				);
	}

	public static int info(CommandContext<CommandSourceStack> context) {
		CommandSourceStack sender = context.getSource();
		PatPatServerRateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		Component text = CommandTextBuilder.startBuilder(
				"ratelimit.set.interval.info",
				TextUtils.literal(rateLimitConfig.getTokenIncrementInterval().toString()).withStyle(ChatFormatting.GOLD)
		).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<CommandSourceStack> context) {
		CommandSourceStack sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		String value = StringArgumentType.getString(context, "value");
		try {
			Time time = Time.of(value);
			if (time.getValue() < 1) {
				Component text = CommandTextBuilder.startBuilder(
						"error.time_less_than",
						TextUtils.literal(time).withStyle(ChatFormatting.GOLD),
						TextUtils.literal("1sec").withStyle(ChatFormatting.GOLD)
				).build();
				sender.sendPatPatFeedback(text);
				return 0;
			}
			rateLimitConfig.setTokenIncrementInterval(time);
			config.saveAsync();
			PatPatServerRateLimitManager.reloadTask();
			Component text = CommandTextBuilder.startBuilder(
					"ratelimit.set.interval",
					TextUtils.literal(time).withStyle(ChatFormatting.GOLD)
			).build();
			sender.sendPatPatFeedback(text);
		} catch (IllegalArgumentException ignored) {
			Component text = CommandTextBuilder.startBuilder(
					"ratelimit.set.interval.time_not_time",
					TextUtils.literal(value).withStyle(ChatFormatting.GOLD)
			).build();
			sender.sendPatPatFeedback(text);
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}

}
