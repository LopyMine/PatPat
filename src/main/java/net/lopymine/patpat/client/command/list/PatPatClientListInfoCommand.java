//~ client_fabric_commands

package net.lopymine.patpat.client.command.list;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.*;
import lombok.experimental.ExtensionMethod;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.list.PatPatClientPlayerListConfig;
import net.lopymine.patpat.client.config.sub.PatPatClientMultiplayerConfig;
import net.lopymine.patpat.common.command.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.ClientCommandExtension;
import static net.lopymine.patpat.client.command.PatPatClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListInfoCommand {

	private PatPatClientListInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("info")
				.executes(PatPatClientListInfoCommand::onInfo);
	}

	private static int onInfo(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientMultiplayerConfig serverConfig = config.getMultiPlayerConfig();
		Map<UUID, String> map = PatPatClientPlayerListConfig.getInstance().getValues();
		PatPatCommonListChangeCommand.sendInfo(map, serverConfig.getListMode(), (component) -> context.sendMsg(component));
		return Command.SINGLE_SUCCESS;
	}

}
