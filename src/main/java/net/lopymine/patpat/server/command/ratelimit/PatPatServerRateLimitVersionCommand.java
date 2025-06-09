package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.CommandExtension;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitVersionCommand {

	private PatPatServerRateLimitVersionCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("version")
				.requires(context -> context.hasPatPatPermission("version"))
				.executes(PatPatServerRateLimitVersionCommand::version);
	}

	public static int version(CommandContext<CommandSourceStack> context) {
		context.getSource().sendPatPatFeedback("Fabric " + PatPat.MOD_VERSION);
		return Command.SINGLE_SUCCESS;
	}

}
