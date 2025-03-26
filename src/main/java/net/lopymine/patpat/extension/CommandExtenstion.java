package net.lopymine.patpat.extension;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandExtenstion {

	private CommandExtenstion() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatFeedback(ServerCommandSource source, Text text, boolean broadcastToOps){
		source.sendFeedback(/*? >=1.20 {*/() -> text/*?} else {*//*text*//*?}*/, broadcastToOps);
	}

	public static void sendPatPatFeedback(ServerCommandSource source, String text, boolean broadcastToOps){
		source.sendFeedback(/*? >=1.20 {*/() -> Text.literal(text)/*?} else {*//*Text.literal(text)*//*?}*/, broadcastToOps);
	}


}
