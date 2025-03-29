package net.lopymine.patpat.manager.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.CommandExtension;

@ExtensionMethod(CommandExtension.class)
public class VersionCommand {

	private VersionCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int version(CommandContext<ServerCommandSource> context) {
		context.getSource().sendPatPatFeedback("Fabric " + PatPat.MOD_VERSION);
		return Command.SINGLE_SUCCESS;
	}

}
