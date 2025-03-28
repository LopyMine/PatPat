package net.lopymine.patpat.manager.server.command.ratelimit.set;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.config.server.RateLimitConfig;
import net.lopymine.patpat.extension.CommandExtension;
import net.minecraft.server.command.ServerCommandSource;

@ExtensionMethod(CommandExtension.class)
public class IncrementCommand {

	private IncrementCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		RateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		sender.sendPatPatFeedback("Token Increment: " + rateLimitConfig.getTokenIncrement(), false);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		int value = IntegerArgumentType.getInteger(context, "value");
		if (value <= 0) {
			sender.sendPatPatFeedback("Increment '%s' can't be less than 1".formatted(value), false);
			return 0;
		}
		rateLimitConfig.setTokenIncrement(value);
		config.save();
		sender.sendPatPatFeedback("Set token increment: " + value, false);
		return Command.SINGLE_SUCCESS;
	}

}
