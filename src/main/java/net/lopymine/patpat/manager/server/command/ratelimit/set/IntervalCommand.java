package net.lopymine.patpat.manager.server.command.ratelimit.set;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.config.server.RateLimitConfig;
import net.lopymine.patpat.config.server.Time;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.manager.server.command.RateLimitManager;
import net.minecraft.server.command.ServerCommandSource;

@ExtensionMethod(CommandExtension.class)
public class IntervalCommand {

	private IntervalCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		RateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		sender.sendPatPatFeedback("Token Interval: " + rateLimitConfig.getTokenIncrementInterval().toString(), false);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		String value = StringArgumentType.getString(context, "value");
		try {
			Time time = Time.of(value);
			if (time.getValue() <= 0) {
				sender.sendPatPatFeedback("Time '%s' can't be less than 1".formatted(time), false);
				return 0;
			}
			rateLimitConfig.setTokenIncrementInterval(time);
			config.save();
			RateLimitManager.reloadTask();
			sender.sendPatPatFeedback("Set token interval: " + time, false);
		} catch (IllegalArgumentException ignored) {
			sender.sendPatPatFeedback("'%s' is not time value (examples: 1sec, 5s, 5min...)".formatted(value), false);
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}

}
