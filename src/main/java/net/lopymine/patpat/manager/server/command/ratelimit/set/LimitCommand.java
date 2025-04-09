package net.lopymine.patpat.manager.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.utils.CommandTextBuilder;

@ExtensionMethod(CommandExtension.class)
public class LimitCommand {

	private LimitCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		RateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		Text text = CommandTextBuilder.startBuilder("ratelimit.set.limit.info", rateLimitConfig.getTokenLimit()).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		int value = IntegerArgumentType.getInteger(context, "value");
		rateLimitConfig.setTokenLimit(value);
		config.saveAsync();
		Text text = CommandTextBuilder.startBuilder("ratelimit.set.limit", value).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

}
