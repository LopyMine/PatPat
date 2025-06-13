package net.lopymine.patpat.extension;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.server.command.PatPatServerCommandManager;
import net.lopymine.patpat.utils.TextUtils;

import net.minecraft.network.chat.*;

public class ClientCommandExtension {

	public static final MutableComponent PATPAT_ID = TextUtils.literal("[§aPatPat/Client§f] ");

	private ClientCommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatFeedback(FabricClientCommandSource source, String feedback) {
		Component text = /*? >=1.20 {*/Component.literal(feedback)/*?} else {*//*Text.of(feedback)*//*?}*/;
		sendPatPatFeedback(source, text);
	}

	public static void sendPatPatFeedback(FabricClientCommandSource source, Component text) {
		source.sendFeedback(PATPAT_ID.copy().append(text));
	}

}
