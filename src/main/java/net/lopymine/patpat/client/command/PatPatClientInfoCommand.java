package net.lopymine.patpat.client.command;

import lombok.experimental.ExtensionMethod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.FabricClientCommandSource;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.CommandText;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;

import static net.fabricmc.fabric.api.client.command./*? if >=1.19 {*/ v2 /*?} else {*/ /*v1 *//*?}*/.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientInfoCommand {

	private static PatPatClientInfoCommand instance;

	public static final String PLATFORM = "Fabric/Client";

	private final MutableComponent platformText;
	private final MutableComponent versionText;
	private final MutableComponent minecraftVersionText;


	private PatPatClientInfoCommand() {
		String version = PatPat.MOD_VERSION + "+" + PatPat.BUILD_CODE_TIME;
		String minecraftVersion = SharedConstants.getCurrentVersion()./*? if >=1.21.6 {*/name/*?} else {*/ /*getName *//*?}*/();
		String debugInformation = "Platform: %s%nMinecraft: %s%nVersion: %s"
				.formatted(PLATFORM, minecraftVersion, version);

		Style style = Style.EMPTY
				.withClickEvent(CommandText.getClickEvent(Action.COPY_TO_CLIPBOARD, debugInformation))
				.withHoverEvent(CommandText.getHoverEvent(HoverEvent.Action.SHOW_TEXT, CommandText.text("info.copy").finish()));

		platformText = CommandText.goldenArgs("info.platform", PLATFORM)
				.finish()
				.withStyle(style);

		versionText = CommandText.goldenArgs("info.version", version)
				.finish()
				.withStyle(style);

		minecraftVersionText = CommandText.goldenArgs("info.minecraft_version", minecraftVersion)
				.finish()
				.withStyle(style);
	}

	public static PatPatClientInfoCommand getInstance() {
		if (instance == null) {
			instance = new PatPatClientInfoCommand();
		}
		return instance;
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("info")
				.executes(context -> PatPatClientInfoCommand.getInstance().version(context));
	}

	public int version(CommandContext<FabricClientCommandSource> context) {
		context.sendMsg(platformText);
		context.sendMsg(minecraftVersionText);
		context.sendMsg(versionText);

		return Command.SINGLE_SUCCESS;
	}

}
