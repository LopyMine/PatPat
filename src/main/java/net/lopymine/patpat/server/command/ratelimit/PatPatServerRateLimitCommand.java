package net.lopymine.patpat.server.command.ratelimit;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.lopymine.patpat.server.command.ratelimit.set.*;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public class PatPatServerRateLimitCommand {

	private PatPatServerRateLimitCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("ratelimit")
				.then(PatPatServerRateLimitInfoCommand.get())
				.then(PatPatServerRateLimitSetCommand.get())
				.then(PatPatServerRateLimitToggleCommand.getEnable())
				.then(PatPatServerRateLimitToggleCommand.getDisable());
	}
}
