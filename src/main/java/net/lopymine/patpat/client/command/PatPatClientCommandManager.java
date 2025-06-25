package net.lopymine.patpat.client.command;

import lombok.experimental.ExtensionMethod;

import net.lopymine.patpat.*;
import net.lopymine.patpat.client.command.list.*;
import net.lopymine.patpat.client.command.mod.PatPatClientModEnableCommand;
import net.lopymine.patpat.extension.TextExtension;

//? >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//?} else {
/*import net.fabricmc.fabric.api.client.command.v1.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
*///?}

@ExtensionMethod(TextExtension.class)
public class PatPatClientCommandManager {

	private PatPatClientCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static final PatLogger LOGGER = PatPat.LOGGER.extend("CommandManager");

	public static void register() {
		/*? >=1.19 {*/
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, environment) -> dispatcher
				/*?} else {*/
				/*ClientCommandManager.DISPATCHER
				 *//*?}*/
				.register(literal("patpat-client")
						.then(PatPatClientListCommand.get())
						.then(PatPatClientModEnableCommand.getOff())
						.then(PatPatClientModEnableCommand.getOn())
				)
				/*? >=1.19 {*/))/*?}*/;
	}
}
