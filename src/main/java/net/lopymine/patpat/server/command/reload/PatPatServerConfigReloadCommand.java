package net.lopymine.patpat.server.command.reload;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.utils.CommandText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerConfigReloadCommand {

	private PatPatServerConfigReloadCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("reload")
				.requires(context -> context.hasPatPatPermission("reload"))
				.executes(PatPatServerConfigReloadCommand::reload);
	}

	public static int reload(CommandContext<CommandSourceStack> context) {
		PatPatConfigManager.reloadServer();
		PatPatServerRateLimitManager.reloadTask();
		Component text = CommandText.text("reload").finish();
		context.sendMsg(text);
		return Command.SINGLE_SUCCESS;
	}
}
