//~ client_fabric_commands

package net.lopymine.patpat.client.command.list;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import static net.lopymine.patpat.common.command.PatPatCommonCommandHelper.literal;

public class PatPatClientListCommand {

	private PatPatClientListCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("list")
				.then(PatPatClientListInfoCommand.get())
				.then(PatPatClientListSetModeCommand.get())
				.then(PatPatClientListChangeCommand.getAdd())
				.then(PatPatClientListChangeCommand.getRemove());
	}

}
