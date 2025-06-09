package net.lopymine.patpat.extension;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.server.command.PatPatServerCommandManager;
import net.minecraft.network.chat.Component;

public class ClientCommandExtension {

	private ClientCommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatFeedback(FabricClientCommandSource source, String feedback) {
		Component text = /*? >=1.20 {*/Component.literal(feedback)/*?} else {*//*Text.of(feedback)*//*?}*/;
		sendPatPatFeedback(source, text);
	}

	public static void sendPatPatFeedback(FabricClientCommandSource source, Component text) {
		source.sendFeedback(PatPatServerCommandManager.PATPAT_ID.copy().append(text));
	}

}
