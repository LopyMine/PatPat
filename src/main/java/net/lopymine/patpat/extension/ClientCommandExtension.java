package net.lopymine.patpat.extension;

import net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.FabricClientCommandSource;

import net.lopymine.patpat.utils.TextUtils;

import net.minecraft.network.chat.*;

import com.mojang.brigadier.context.CommandContext;

public class ClientCommandExtension {

	public static final MutableComponent PATPAT_ID = TextUtils.literal("[§aPatPat/Client§f] ");

	private ClientCommandExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendMsg(CommandContext<FabricClientCommandSource> context, String feedback) {
		Component text = TextUtils.literal(feedback);
		sendMsg(context, text);
	}

	public static void sendMsg(CommandContext<FabricClientCommandSource> context, Component text) {
		context.getSource().sendFeedback(PATPAT_ID.copy().append(text));
	}

}
