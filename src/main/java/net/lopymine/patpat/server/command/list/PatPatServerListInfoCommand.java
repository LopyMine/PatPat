package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.common.command.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.server.config.list.PatPatServerPlayerListConfig;

import java.util.*;

import static net.lopymine.patpat.server.command.PatPatServerCommandManager.permission;
import static net.minecraft.commands.Commands.literal;


@ExtensionMethod(CommandExtension.class)
public class PatPatServerListInfoCommand {

	private PatPatServerListInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("info")
				.requires(permission("list.info"))
				.executes(PatPatServerListInfoCommand::onInfo);
	}

	private static int onInfo(CommandContext<CommandSourceStack> context) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		Map<UUID, String> map = PatPatServerPlayerListConfig.getInstance().getValues();
		PatPatCommonListChangeCommand.sendInfo(map, config.getListMode(), (component) -> context.sendMsg(component));
		return Command.SINGLE_SUCCESS;
	}

}
