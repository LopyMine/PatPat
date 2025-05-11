package net.lopymine.patpat.extension;

import net.minecraft.text.Text;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.server.command.PatPatServerCommandManager;

public class ClientCommandExtension {

	private ClientCommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatFeedback(FabricClientCommandSource source, String feedback) {
		Text text = /*? >=1.20 {*/Text.literal(feedback)/*?} else {*//*Text.of(feedback)*//*?}*/;
		sendPatPatFeedback(source, text);
	}

	public static void sendPatPatFeedback(FabricClientCommandSource source, Text text) {
		source.sendFeedback(PatPatServerCommandManager.PATPAT_ID.copy().append(text));
	}

}
