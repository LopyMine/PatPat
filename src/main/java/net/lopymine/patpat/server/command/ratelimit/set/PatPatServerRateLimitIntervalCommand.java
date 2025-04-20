package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.common.config.time.Time;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.utils.CommandTextBuilder;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitIntervalCommand {

	private PatPatServerRateLimitIntervalCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerRateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		Text text = CommandTextBuilder.startBuilder("ratelimit.set.interval.info", rateLimitConfig.getTokenIncrementInterval().toString()).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		String value = StringArgumentType.getString(context, "value");
		try {
			Time time = Time.of(value);
			if (time.getValue() < 1) {
				Text text = CommandTextBuilder.startBuilder("ratelimit.set.interval.value_less_one", time).build();
				sender.sendPatPatFeedback(text);
				return 0;
			}
			rateLimitConfig.setTokenIncrementInterval(time);
			config.saveAsync();
			PatPatServerRateLimitManager.reloadTask();
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
