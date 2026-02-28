package net.lopymine.patpat.server.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.function.Predicate;
import lombok.experimental.ExtensionMethod;

import net.lopymine.patpat.*;
import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.logger.PatLogger;
import net.lopymine.patpat.server.command.info.PatPatServerInfoCommand;
import net.lopymine.patpat.server.command.list.*;
import net.lopymine.patpat.server.command.ratelimit.*;
import net.lopymine.patpat.server.command.reload.PatPatServerConfigReloadCommand;
import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.commands.*;
import net.minecraft.network.chat.MutableComponent;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerCommandManager {

	public static final PatLogger LOGGER = PatPat.LOGGER.extend("CommandManager");

	public static final MutableComponent PATPAT_ID = TextUtils.literal("[§aPatPat§f] ");

	private PatPatServerCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ServerMultiLoader.getInstance().registerOnServerStart(PatPatServerRateLimitManager::runTask);
		ServerMultiLoader.getInstance().registerOnServerStop(PatPatServerRateLimitManager::stopTask);

		ServerMultiLoader.getInstance().registerServerCommands((dispatcher) -> dispatcher.register(
				literal("patpat")
						.then(PatPatServerInfoCommand.get())
						.then(PatPatServerListCommand.get())
						.then(PatPatServerRateLimitCommand.get())
						.then(PatPatServerConfigReloadCommand.get()))
		);

		ServerMultiLoader.getInstance().registerPermission(PatPatServerConfig.getInstance().getRateLimitConfig().getPermissionBypass());
	}

	public static Predicate<CommandSourceStack> permission(String permission) {
		String id = "%s.command.%s".formatted(PatPat.MOD_ID, permission);
		ServerMultiLoader.getInstance().registerPermission("command." + permission);
		return (context) -> context.hasPatPatPermission(id);
	}
}
