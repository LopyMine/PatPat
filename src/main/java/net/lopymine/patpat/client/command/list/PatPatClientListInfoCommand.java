package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.common.command.list.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

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
