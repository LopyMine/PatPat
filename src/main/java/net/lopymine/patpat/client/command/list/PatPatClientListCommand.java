//~ client_fabric_commands

package net.lopymine.patpat.client.command.list;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.lopymine.patpat.client.command.PatPatClientCommandManager.literal;

public class PatPatClientListCommand {

	private PatPatClientListCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("list")
				.then(PatPatClientListInfoCommand.get())
				.then(PatPatClientListSetModeCommand.get())
				.then(PatPatClientListChangeCommand.getAdd())
				.then(PatPatClientListChangeCommand.getRemove());
	}

}
