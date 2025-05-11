package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.CommandExtension;

import static net.minecraft.server.command.CommandManager.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerRateLimitVersionCommand {

	private PatPatServerRateLimitVersionCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<ServerCommandSource> get() {
		return literal("version")
				.requires(context -> context.hasPatPatPermission("version"))
				.executes(PatPatServerRateLimitVersionCommand::version);
	}

	public static int version(CommandContext<ServerCommandSource> context) {
		context.getSource().sendPatPatFeedback("Fabric " + PatPat.MOD_VERSION);
		return Command.SINGLE_SUCCESS;
	}

}
