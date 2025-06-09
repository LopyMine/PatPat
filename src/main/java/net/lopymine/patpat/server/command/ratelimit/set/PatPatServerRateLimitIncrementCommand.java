package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.utils.CommandTextBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.lopymine.patpat.server.command.ratelimit.set.PatPatServerRateLimitSetCommand.VALUE_KEY;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitIncrementCommand {

	private PatPatServerRateLimitIncrementCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("increment")
				.requires(context -> context.hasPatPatPermission("ratelimit.set.increment"))
				.executes(PatPatServerRateLimitIncrementCommand::info)
				.then(argument(VALUE_KEY, IntegerArgumentType.integer(1))
						.executes(PatPatServerRateLimitIncrementCommand::set)
				);
	}

	public static int info(CommandContext<CommandSourceStack> context) {
		CommandSourceStack sender = context.getSource();
		PatPatServerRateLimitConfig rateLimitConfig = PatPatServerConfig.getInstance().getRateLimitConfig();
		Component text = CommandTextBuilder.startBuilder("ratelimit.set.increment.info", rateLimitConfig.getTokenIncrement()).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int set(CommandContext<CommandSourceStack> context) {
		CommandSourceStack sender = context.getSource();
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		PatPatServerRateLimitConfig rateLimitConfig = config.getRateLimitConfig();
		int value = IntegerArgumentType.getInteger(context, "value");
		rateLimitConfig.setTokenIncrement(value);
		config.saveAsync();
		Component text = CommandTextBuilder.startBuilder("ratelimit.set.increment", value).build();
		sender.sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

}
