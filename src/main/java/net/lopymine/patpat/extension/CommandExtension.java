package net.lopymine.patpat.extension;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.lopymine.patpat.server.command.PatPatServerCommandManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandExtension {

	private CommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatFeedback(CommandSourceStack source, String feedback) {
		sendPatPatFeedback(source, feedback, false);
	}

	public static void sendPatPatFeedback(CommandSourceStack source, String feedback, boolean broadcastToOps) {
		Component text = /*? >=1.20 {*/Component.literal(feedback)/*?} else {*//*Text.of(feedback)*//*?}*/;
		sendPatPatFeedback(source, text, broadcastToOps);
	}

	public static void sendPatPatFeedback(CommandSourceStack source, Component text) {
		sendPatPatFeedback(source, text, false);
	}

	public static void sendPatPatFeedback(CommandSourceStack source, Component text, boolean broadcastToOps) {
		source.sendSuccess(/*? >=1.20 {*/() -> /*?}*/ PatPatServerCommandManager.PATPAT_ID.copy().append(text), broadcastToOps);
	}

	public static boolean hasPatPatPermission(CommandSourceStack context, String permission) {
		return hasPermission(context, PatPatServerCommandManager.getPermission(permission));
	}

	public static boolean hasPermission(CommandSourceStack context, String permission) {
		return hasPermission(context, permission, 2);
	}

	public static boolean hasPatPatPermission(CommandSourceStack context, String permission, int defaultLevel) {
		return hasPermission(context, PatPatServerCommandManager.getPermission(permission), defaultLevel);
	}

	public static boolean hasPermission(CommandSourceStack context, String permission, int defaultLevel) {
		//? <1.17.1 {
		/*return context.hasPermissionLevel(defaultLevel);
		 *///?} else {
		return Permissions.check(context, permission, defaultLevel);
		//?}
	}

}
