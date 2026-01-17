//~ client_fabric_commands

package net.lopymine.patpat.entrypoint.fabric.loader;

//? if fabric {

/*import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
import net.lopymine.patpat.entrypoint.loader.IModLoader;

//? >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.*;
//?} else {
/^import net.fabricmc.fabric.api.client.command.v1.*;
 ^///?}

public abstract class FabricSillyModLoader implements IModLoader {

	@Override
	public void registerClientCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		//? if >=1.19 {
		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, environment) -> consumer.accept(dispatcher)
		);
		//?} else {
		/^consumer.accept(ClientCommandManager.DISPATCHER);
		 ^///?}
	}

}
*///?}
