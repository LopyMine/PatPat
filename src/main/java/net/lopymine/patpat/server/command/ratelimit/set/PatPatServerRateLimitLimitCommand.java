package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.utils.CommandTextBuilder;

import static net.lopymine.patpat.server.command.ratelimit.set.PatPatServerRateLimitSetCommand.VALUE_KEY;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitLimitCommand {

	private PatPatServerRateLimitLimitCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<ServerCommandSource> get() {
		return literal("limit")
				.requires(context -> context.hasPatPatPermission("ratelimit.set.limit"))
				.executes(PatPatServerRateLimitLimitCommand::info)
				.then(argument(VALUE_KEY, IntegerArgumentType.integer(1))
						.executes(PatPatServerRateLimitLimitCommand::set)
				);
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerRateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		Text text = CommandTextBuilder.startBuilder("ratelimit.set.limit.info", rateLimitConfig.getTokenLimit()).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		int value = IntegerArgumentType.getInteger(context, "value");
		rateLimitConfig.setTokenLimit(value);
		config.saveAsync();
		Text text = CommandTextBuilder.startBuilder("ratelimit.set.limit", value).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

}
