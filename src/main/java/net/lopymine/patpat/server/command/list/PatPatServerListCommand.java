package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.lopymine.patpat.extension.*;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerListCommand {

	private PatPatServerListCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("list")
				.requires(context -> context.hasPatPatPermission("list"))
				.then(PatPatServerListInfoCommand.get())
				.then(PatPatServerListSetModeCommand.get())
				.then(PatPatServerListChangeCommand.getAdd())
				.then(PatPatServerListChangeCommand.getRemove());
	}

}
