package net.lopymine.patpat.server.command.reload;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.utils.CommandTextBuilder;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerConfigReloadCommand {

	private PatPatServerConfigReloadCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int reload(CommandContext<ServerCommandSource> context) {
		PatPatConfigManager.reloadServer();
		PatPatServerRateLimitManager.reloadTask();
		Text text = CommandTextBuilder.startBuilder("reload.success").build();
		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}
}
