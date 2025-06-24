package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.FabricClientCommandSource;

import net.lopymine.patpat.client.config.*;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.command.list.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.ClientCommandExtension;

import java.util.*;

import static net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListInfoCommand {

	private PatPatClientListInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("get")
				.executes(PatPatClientListInfoCommand::onInfo);
	}

	private static int onInfo(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientMultiplayerConfig serverConfig = config.getMultiPlayerConfig();
		Map<UUID, String> map = PatPatClientPlayerListConfig.getInstance().getMap();
		PatPatCommonListChangeCommand.sendInfo(map, serverConfig.getListMode(), (component) -> context.sendMsg(component));
		return Command.SINGLE_SUCCESS;
	}

}
