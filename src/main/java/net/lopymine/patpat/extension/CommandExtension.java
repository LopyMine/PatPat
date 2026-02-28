package net.lopymine.patpat.extension;

//import me.lucko.fabric.api.permissions.v0.Permissions;
import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.lopymine.patpat.server.command.PatPatServerCommandManager;
import net.lopymine.patpat.utils.*;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import com.mojang.brigadier.context.CommandContext;

public class CommandExtension {

	private CommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendMsg(CommandContext<CommandSourceStack> source, String feedback) {
		sendMsg(source, feedback, false);
	}

	public static void sendMsg(CommandContext<CommandSourceStack> source, String feedback, boolean broadcastToOps) {
		sendMsg(source, TextUtils.literal(feedback), broadcastToOps);
	}

	public static void sendMsg(CommandContext<CommandSourceStack> source, Component text) {
		sendMsg(source, text, false);
	}

	public static void sendMsg(CommandContext<CommandSourceStack> source, Component text, boolean broadcastToOps) {
		source.getSource().sendSuccess(/*? if >=1.20 {*/() -> /*?}*/ PatPatServerCommandManager.PATPAT_ID.copy().append(text), broadcastToOps);
	}

	public static boolean hasPatPatPermission(CommandSourceStack context, String permission) {
		return ServerMultiLoader.getInstance().hasPermission(context.getPlayer(), permission);
	}

}
