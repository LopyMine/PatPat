package net.lopymine.patpat.extension;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import net.lopymine.patpat.manager.server.PatPatServerCommandManager;

public class CommandExtension {

	private CommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatFeedback(ServerCommandSource source, String feedback) {
		sendPatPatFeedback(source, feedback, false);
	}

	public static void sendPatPatFeedback(ServerCommandSource source, String feedback, boolean broadcastToOps) {
		Text text = /*? >=1.20 {*/Text.literal(feedback)/*?} else {*//*Text.of(feedback)*//*?}*/;
		sendPatPatFeedback(source, text, broadcastToOps);
	}

	public static void sendPatPatFeedback(ServerCommandSource source, Text text) {
		sendPatPatFeedback(source, text, false);
	}

	public static void sendPatPatFeedback(ServerCommandSource source, Text text, boolean broadcastToOps) {
		source.sendFeedback(/*? >=1.20 {*/() -> /*?}*/ PatPatServerCommandManager.PATPAT_ID.copy().append(text), broadcastToOps);
	}

	public static boolean hasPatPatPermission(ServerCommandSource context, String permission) {
		return hasPermission(context, PatPatServerCommandManager.getPermission(permission));
	}

	public static boolean hasPermission(ServerCommandSource context, String permission) {
		return hasPermission(context, permission, 2);
	}

	public static boolean hasPatPatPermission(ServerCommandSource context, String permission, int defaultLevel) {
		return hasPermission(context, PatPatServerCommandManager.getPermission(permission), defaultLevel);
	}

	public static boolean hasPermission(ServerCommandSource context, String permission, int defaultLevel) {
		return Permissions.check(context, permission, defaultLevel);
	}

}
