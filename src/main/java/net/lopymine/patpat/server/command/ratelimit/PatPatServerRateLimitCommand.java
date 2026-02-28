package net.lopymine.patpat.server.command.ratelimit;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.server.command.ratelimit.set.*;
import net.minecraft.commands.CommandSourceStack;

import static net.lopymine.patpat.server.command.PatPatServerCommandManager.permission;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitCommand {

	private PatPatServerRateLimitCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("ratelimit")
				.requires(permission("ratelimit"))
				.then(PatPatServerRateLimitInfoCommand.get())
				.then(PatPatServerRateLimitSetCommand.get())
				.then(PatPatServerRateLimitToggleCommand.getEnable())
				.then(PatPatServerRateLimitToggleCommand.getDisable());
	}
}
