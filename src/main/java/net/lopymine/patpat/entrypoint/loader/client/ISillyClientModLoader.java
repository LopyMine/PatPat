//~ client_fabric_commands

package net.lopymine.patpat.entrypoint.loader.client;

import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
import net.minecraft.commands.CommandSourceStack;

public interface ISillyClientModLoader {

	void registerClientCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer);

}
