package net.lopymine.patpat.client.command.mod;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.FabricClientCommandSource;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.CommandText;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import static net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientModEnableCommand {

	private PatPatClientModEnableCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> getOn() {
		return literal("on")
				.executes(context -> switchPatPatState(context, true));
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> getOff() {
		return literal("off")
				.executes(context -> switchPatPatState(context, false));
	}

	private static int switchPatPatState(CommandContext<FabricClientCommandSource> context, boolean state) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		MutableComponent text;
		if (config.getMainConfig().isModEnabled() != state) {
			config.getMainConfig().setModEnabled(state);
			config.saveAsync();
			text = CommandText.text(state ? "on.success" : "off.success").finish()
					.withStyle(state ? ChatFormatting.GREEN : ChatFormatting.RED);
		} else {
			text = CommandText.text(state ? "on.already" : "off.already").finish();
		}
		context.sendMsg(text);
		return Command.SINGLE_SUCCESS;
	}
}
