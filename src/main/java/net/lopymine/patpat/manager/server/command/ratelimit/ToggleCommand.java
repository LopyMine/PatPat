package net.lopymine.patpat.manager.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.manager.server.command.RateLimitManager;
import net.lopymine.patpat.utils.CommandTextBuilder;

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
			Text text = CommandTextBuilder.startBuilder("ratelimit.%s.already".formatted(toggle ? "enable" : "disable")).build();
			context.getSource().sendPatPatFeedback(text, false);
			return 0;
		}
		rateLimitConfig.setEnabled(toggle);
		config.save();
		RateLimitManager.reloadTask();
		Text text = CommandTextBuilder.startBuilder("ratelimit." + (toggle ? "enable" : "disable")).build();
		context.getSource().sendPatPatFeedback(text, false);
		return Command.SINGLE_SUCCESS;
	}
}
