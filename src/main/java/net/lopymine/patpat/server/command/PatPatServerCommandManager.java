package net.lopymine.patpat.server.command;

import lombok.experimental.ExtensionMethod;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.command.info.PatPatServerInfoCommand;
import net.lopymine.patpat.server.command.list.*;
import net.lopymine.patpat.server.command.ratelimit.*;
import net.lopymine.patpat.server.command.reload.PatPatServerConfigReloadCommand;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.utils.TextUtils;
import net.minecraft.network.chat.MutableComponent;

import static net.minecraft.commands.Commands.literal;

//? >=1.19 {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//?} else {
/*import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
 *///?}


@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerCommandManager {

	public static final MutableComponent PATPAT_ID = TextUtils.literal("[§aPatPat§f] ");

	private PatPatServerCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> PatPatServerRateLimitManager.runTask());
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> PatPatServerRateLimitManager.stopTask());

		CommandRegistrationCallback.EVENT.register((/*? >=1.19 {*/(dispatcher, registryAccess, environment)/*?} else {*//*(dispatcher, dedicated)*//*?}*/ -> {
			//? <=1.18.2 {
			/*if (!dedicated) {
				return;
			}
			*///?}
			dispatcher.register(literal("patpat")
					.then(PatPatServerInfoCommand.get())
					.then(PatPatServerListCommand.get())
					.then(PatPatServerRateLimitCommand.get())
					.then(PatPatServerConfigReloadCommand.get())
			);
		}));
	}

	public static String getPermission(String permission) {
		return "%s.%s".formatted(PatPat.MOD_ID, permission);
	}
}
