package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.lopymine.patpat.extension.*;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerRateLimitSetCommand {

	public static final String VALUE_KEY = "value";

	private PatPatServerRateLimitSetCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("set")
				.then(PatPatServerRateLimitIntervalCommand.get())
				.then(PatPatServerRateLimitIncrementCommand.get())
				.then(PatPatServerRateLimitLimitCommand.get());
	}


}
