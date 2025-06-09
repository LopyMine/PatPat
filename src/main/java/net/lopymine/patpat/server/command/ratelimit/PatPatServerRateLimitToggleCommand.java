package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.utils.CommandTextBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitToggleCommand {

	private PatPatServerRateLimitToggleCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> getEnable() {
		return literal("enable")
				.requires(context -> context.hasPatPatPermission("ratelimit.toggle"))
				.executes(PatPatServerRateLimitToggleCommand::enable);
	}

	public static LiteralArgumentBuilder<CommandSourceStack> getDisable() {
		return literal("disable")
				.requires(context -> context.hasPatPatPermission("ratelimit.toggle"))
				.executes(PatPatServerRateLimitToggleCommand::disable);
	}

	public static int enable(CommandContext<CommandSourceStack> context) {
		return toggle(context, true);
	}

	public static int disable(CommandContext<CommandSourceStack> context) {
		return toggle(context, false);
	}

	public static int toggle(CommandContext<CommandSourceStack> context, boolean toggle) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		if (rateLimitConfig.isEnabled() == toggle) {
			Component text = CommandTextBuilder.startBuilder("ratelimit.%s.already".formatted(toggle ? "enable" : "disable")).build();
			context.getSource().sendPatPatFeedback(text, false);
			return 0;
		}
		rateLimitConfig.setEnabled(toggle);
		config.saveAsync();
		PatPatServerRateLimitManager.reloadTask();
		Component text = CommandTextBuilder.startBuilder("ratelimit." + (toggle ? "enable" : "disable")).build();
		context.getSource().sendPatPatFeedback(text, false);
		return Command.SINGLE_SUCCESS;
	}
}
