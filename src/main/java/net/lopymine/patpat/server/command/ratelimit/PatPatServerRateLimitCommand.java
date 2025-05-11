package net.lopymine.patpat.server.command.ratelimit;

import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.lopymine.patpat.server.command.ratelimit.set.*;

import static net.minecraft.server.command.CommandManager.literal;

public class PatPatServerRateLimitCommand {

	private PatPatServerRateLimitCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<ServerCommandSource> get() {
		return literal("ratelimit")
				.then(PatPatServerRateLimitVersionCommand.get())
				.then(PatPatServerRateLimitInfoCommand.get())
				.then(PatPatServerRateLimitSetCommand.get())
				.then(PatPatServerRateLimitToggleCommand.getEnable())
				.then(PatPatServerRateLimitToggleCommand.getDisable());
	}
}
