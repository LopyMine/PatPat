package net.lopymine.patpat.manager.server.command.ratelimit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.config.server.RateLimitConfig;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.manager.server.command.RateLimitManager;
import net.minecraft.server.command.ServerCommandSource;

@ExtensionMethod(CommandExtension.class)
public class ToggleCommand {

	private ToggleCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int enable(CommandContext<ServerCommandSource> context) {
		return toggle(context, true);
	}

	public static int disable(CommandContext<ServerCommandSource> context) {
		return toggle(context, false);
	}

	public static int toggle(CommandContext<ServerCommandSource> context, boolean toggle) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		if (rateLimitConfig.isEnabled() == toggle) {
			context.getSource().sendPatPatFeedback("RateLimit is already " + (toggle ? "enabled" : "disabled"), false);
			return Command.SINGLE_SUCCESS; // TODO: Стоит ли здесь возвращать 1 или переделать на 0?
		}
		rateLimitConfig.setEnabled(toggle);
		config.save();
		RateLimitManager.reloadTask();
		context.getSource().sendPatPatFeedback("RateLimit " + (toggle ? "enabled" : "disabled"), false);
		return Command.SINGLE_SUCCESS;
	}
}
