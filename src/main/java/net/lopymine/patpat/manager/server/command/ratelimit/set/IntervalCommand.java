package net.lopymine.patpat.manager.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.manager.server.command.RateLimitManager;
import net.lopymine.patpat.utils.CommandTextBuilder;

@ExtensionMethod(CommandExtension.class)
public class IntervalCommand {

	private IntervalCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		RateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		Text text = CommandTextBuilder.startBuilder("ratelimit.set.interval.info", rateLimitConfig.getTokenIncrementInterval().toString()).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		String value = StringArgumentType.getString(context, "value");
		try {
			Time time = Time.of(value);
			if (time.getValue() < 1) {
				Text text = CommandTextBuilder.startBuilder("ratelimit.set.interval.value_less_one", time).build();
				sender.sendPatPatFeedback(text);
				return 0;
			}
			rateLimitConfig.setTokenIncrementInterval(time);
			config.save();
			RateLimitManager.reloadTask();
			Text text = CommandTextBuilder.startBuilder("ratelimit.set.interval", time).build();
			sender.sendPatPatFeedback(text);
		} catch (IllegalArgumentException ignored) {
			Text text = CommandTextBuilder.startBuilder("ratelimit.set.interval.wrong_type", value).build();
			sender.sendPatPatFeedback(text);
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}

}
