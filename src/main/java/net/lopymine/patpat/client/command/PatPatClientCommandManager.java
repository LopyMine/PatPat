//~ client_fabric_commands

package net.lopymine.patpat.client.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.*;
import lombok.experimental.ExtensionMethod;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.command.ignore.PatPatClientIgnoreCommand;
import net.lopymine.patpat.client.command.info.PatPatClientInfoCommand;
import net.lopymine.patpat.client.command.list.PatPatClientListCommand;
import net.lopymine.patpat.client.command.mod.PatPatClientModEnableCommand;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.lopymine.patpat.extension.TextExtension;
import net.lopymine.patpat.logger.PatLogger;

@ExtensionMethod(TextExtension.class)
public class PatPatClientCommandManager {

	public static final PatLogger LOGGER = PatPat.LOGGER.extend("CommandManager");

	private PatPatClientCommandManager() {
		throw new IllegalStateException("Manager class");
	}

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

	public static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
}
