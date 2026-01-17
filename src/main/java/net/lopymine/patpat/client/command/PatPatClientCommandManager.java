//~ client_fabric_commands

package net.lopymine.patpat.client.command;

import lombok.experimental.ExtensionMethod;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.command.ignore.PatPatClientIgnoreCommand;
import net.lopymine.patpat.client.command.info.PatPatClientInfoCommand;
import net.lopymine.patpat.client.command.list.*;
import net.lopymine.patpat.client.command.mod.PatPatClientModEnableCommand;
import net.lopymine.patpat.entrypoint.MultiLoader;
import net.lopymine.patpat.extension.TextExtension;
import net.lopymine.patpat.logger.PatLogger;
import static net.lopymine.patpat.common.command.PatPatCommonCommandHelper.literal;

@ExtensionMethod(TextExtension.class)
public class PatPatClientCommandManager {

	private PatPatClientCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static final PatLogger LOGGER = PatPat.LOGGER.extend("CommandManager");

	public static void register() {
		MultiLoader.getInstance().registerClientCommands((dispatcher) -> {
			dispatcher.register(literal("patpat-client")
					.then(PatPatClientListCommand.get())
					.then(PatPatClientModEnableCommand.getOff())
					.then(PatPatClientModEnableCommand.getOn())
					.then(PatPatClientInfoCommand.get())
					.then(PatPatClientIgnoreCommand.get())
			);
		});
	}
}
