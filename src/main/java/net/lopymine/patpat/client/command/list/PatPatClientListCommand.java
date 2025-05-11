package net.lopymine.patpat.client.command.list;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PatPatClientListCommand {

	private PatPatClientListCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("list")
				.then(PatPatClientListSetModeCommand.get())
				.then(PatPatClientListChangeCommand.getAdd())
				.then(PatPatClientListChangeCommand.getRemove());
	}

}
