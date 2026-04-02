//~ client_fabric_commands

package net.lopymine.patpat.extension;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.utils.TextUtils;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.network.chat.*;

import com.mojang.brigadier.context.CommandContext;

public class ClientCommandExtension {

	public static final MutableComponent PATPAT_ID = TextUtils.literal("[§a%s/Client§f] ".formatted(PatPat.MOD_NAME));

	private ClientCommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendMsg(CommandContext<FabricClientCommandSource> context, String feedback) {
		Component text = TextUtils.literal(feedback);
		sendMsg(context, text);
	}

	public static void sendMsg(CommandContext<FabricClientCommandSource> context, Component text) {
		//? if fabric {
		context.getSource().sendFeedback(PATPAT_ID.copy().append(text));
		//?} else {
		/*context.getSource().sendSystemMessage(PATPAT_ID.copy().append(text));
		*///?}
	}

}
