//~ client_fabric_commands

package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.client.command.argument.*;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.config.sub.PatPatClientMultiplayerConfig;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.network.chat.*;

import net.minecraft.commands.CommandSourceStack;
import static net.lopymine.patpat.common.command.PatPatCommonCommandHelper.literal;
import static net.lopymine.patpat.common.command.PatPatCommonCommandHelper.argument;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListSetModeCommand {

	private PatPatClientListSetModeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("set")
						.then(argument("mode", ListModeArgumentType.listMode())
								.executes(PatPatClientListSetModeCommand::onSetListMode));
	}

	private static int onSetListMode(CommandContext<CommandSourceStack> context) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		ListMode mode = ListModeArgumentType.getListMode(context, "mode");
		PatPatClientMultiplayerConfig serverConfig = config.getMultiPlayerConfig();

		Component text;
		if (serverConfig.getListMode() == mode) {
			text = CommandText.text("list.set.already", mode.getText()).finish();
		} else {
			text = CommandText.text("list.set.success", mode.getText()).finish();
			serverConfig.setListMode(mode);
			config.saveAsync();
		}

		context.sendMsg(text);
		return Command.SINGLE_SUCCESS;
	}

}
