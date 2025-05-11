package net.lopymine.patpat.server.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.lopymine.patpat.extension.*;

import static net.minecraft.server.command.CommandManager.literal;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerRateLimitSetCommand {

	public static final String VALUE_KEY = "value";

	private PatPatServerRateLimitSetCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<ServerCommandSource> get() {
		return literal("set")
				.then(PatPatServerRateLimitIntervalCommand.get())
				.then(PatPatServerRateLimitIncrementCommand.get())
				.then(PatPatServerRateLimitLimitCommand.get());
	}


}
