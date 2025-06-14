package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.common.command.list.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;

import java.util.*;

import static net.minecraft.commands.Commands.literal;


@ExtensionMethod(CommandExtension.class)
public class PatPatServerListInfoCommand {

	private PatPatServerListInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("info")
				.requires(context -> context.hasPatPatPermission("list.info"))
				.executes(PatPatServerListInfoCommand::onInfo);
	}

	private static int onInfo(CommandContext<CommandSourceStack> context) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		Map<UUID, String> map = PatPatServerPlayerListConfig.getInstance().getMap();
		PatPatCommonListChangeCommand.sendInfo(map, config.getListMode(), (component) -> context.sendMsg(component, true));
		return Command.SINGLE_SUCCESS;
	}

}
