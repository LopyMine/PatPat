package net.lopymine.patpat.client.command.list;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.ClientCommandManager.literal;

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
