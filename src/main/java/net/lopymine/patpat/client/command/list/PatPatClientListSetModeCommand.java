package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.client.command.argument.*;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListSetModeCommand {

	private PatPatClientListSetModeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("set")
						.then(argument("mode", ListModeArgumentType.listMode())
								.executes(PatPatClientListSetModeCommand::onSetListMode));
	}

	private static int onSetListMode(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		ListMode mode = ListModeArgumentType.getListMode(context, "mode");
		config.getServerConfig().setListMode(mode);
		config.saveAsync();

		Text text = CommandTextBuilder.startBuilder("list.mode.success", mode.getText()).build();

		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

}
