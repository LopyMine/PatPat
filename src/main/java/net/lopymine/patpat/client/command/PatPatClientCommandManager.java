//~ client_fabric_commands

package net.lopymine.patpat.client.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.*;
import lombok.experimental.ExtensionMethod;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.command.ignore.PatPatClientIgnoreCommand;
import net.lopymine.patpat.client.command.info.PatPatClientInfoCommand;
import net.lopymine.patpat.client.command.list.*;
import net.lopymine.patpat.client.command.mod.PatPatClientModEnableCommand;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.lopymine.patpat.extension.TextExtension;
import net.lopymine.patpat.logger.PatLogger;
import net.minecraft.commands.CommandSourceStack;

@ExtensionMethod(TextExtension.class)
public class PatPatClientCommandManager {

	private PatPatClientCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static final PatLogger LOGGER = PatPat.LOGGER.extend("CommandManager");

	public static void register() {
		ClientMultiLoader.getInstance().registerClientCommands((dispatcher) -> {
			dispatcher.register(literal("patpat-client")
					.then(PatPatClientListCommand.get())
					.then(PatPatClientModEnableCommand.getOff())
					.then(PatPatClientModEnableCommand.getOn())
					.then(PatPatClientInfoCommand.get())
					.then(PatPatClientIgnoreCommand.get())
			);
		});
	}

	public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
}
