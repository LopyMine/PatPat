package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.utils.CommandText;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;

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
		String key = toggle ? "enable" : "disable";

		if (rateLimitConfig.isEnabled() == toggle) {
			MutableComponent text = CommandText.text("ratelimit.%s.already".formatted(key))
					.finish();
			context.sendMsg(text);
			return 0;
		}
		rateLimitConfig.setEnabled(toggle);
		config.saveAsync();
		PatPatServerRateLimitManager.reloadTask();
		MutableComponent text = CommandText.text("ratelimit.%s.success".formatted(key))
				.finish()
				.withStyle(toggle ? ChatFormatting.GREEN : ChatFormatting.RED);
		context.sendMsg(text);
		return Command.SINGLE_SUCCESS;
	}
}
