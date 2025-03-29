package net.lopymine.patpat.manager.server.command;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.utils.CommandTextBuilder;

@ExtensionMethod(CommandExtension.class)
public class ConfigReloadCommand {

	private ConfigReloadCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int reload(CommandContext<ServerCommandSource> context) {
		PatPatConfigManager.reload();
		RateLimitManager.reloadTask();
		Text text = CommandTextBuilder.startBuilder("reload.success").build();
		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}
}
