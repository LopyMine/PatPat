package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.utils.CommandTextBuilder;

import static net.minecraft.server.command.CommandManager.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitToggleCommand {

	private PatPatServerRateLimitToggleCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<ServerCommandSource> getEnable() {
		return literal("enable")
				.requires(context -> context.hasPatPatPermission("ratelimit.toggle"))
				.executes(PatPatServerRateLimitToggleCommand::enable);
	}

	public static LiteralArgumentBuilder<ServerCommandSource> getDisable() {
		return literal("disable")
				.requires(context -> context.hasPatPatPermission("ratelimit.toggle"))
				.executes(PatPatServerRateLimitToggleCommand::disable);
	}

	public static int enable(CommandContext<ServerCommandSource> context) {
		return toggle(context, true);
	}

	public static int disable(CommandContext<ServerCommandSource> context) {
		return toggle(context, false);
	}

	public static int toggle(CommandContext<ServerCommandSource> context, boolean toggle) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		if (rateLimitConfig.isEnabled() == toggle) {
			Text text = CommandTextBuilder.startBuilder("ratelimit.%s.already".formatted(toggle ? "enable" : "disable")).build();
			context.getSource().sendPatPatFeedback(text, false);
			return 0;
		}
		rateLimitConfig.setEnabled(toggle);
		config.saveAsync();
		PatPatServerRateLimitManager.reloadTask();
		Text text = CommandTextBuilder.startBuilder("ratelimit." + (toggle ? "enable" : "disable")).build();
		context.getSource().sendPatPatFeedback(text, false);
		return Command.SINGLE_SUCCESS;
	}
}
