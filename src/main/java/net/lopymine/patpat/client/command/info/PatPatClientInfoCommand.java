//~ client_fabric_commands

package net.lopymine.patpat.client.command.info;

import lombok.experimental.ExtensionMethod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.EarlyCommonMultiLoader;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.CommandText;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;

//? if forge || neoforge {
/*import net.minecraft.commands.FabricClientCommandSource;
*///?} else {
//? if <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
 //?}
//?}

import static net.lopymine.patpat.client.command.PatPatClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientInfoCommand {

	private static PatPatClientInfoCommand INSTANCE;

	private final MutableComponent platformText;
	private final MutableComponent versionText;
	private final MutableComponent minecraftVersionText;


	private PatPatClientInfoCommand() {
		String platform = EarlyCommonMultiLoader.getInstance().getFullPlatform();

		String version = PatPat.MOD_VERSION + "+" + PatPat.BUILD_CODE_TIME;
		String minecraftVersion = SharedConstants.getCurrentVersion()./*? if >=1.21.6 {*/name/*?} else {*/ /*getName *//*?}*/();
		String debugInformation = "Platform: %s%nMinecraft: %s%nVersion: %s"
				.formatted(platform, minecraftVersion, version);

		Style style = Style.EMPTY
				.withClickEvent(CommandText.getClickEvent(Action.COPY_TO_CLIPBOARD, debugInformation))
				.withHoverEvent(CommandText.getHoverEvent(HoverEvent.Action.SHOW_TEXT, CommandText.text("info.copy").finish()));

		this.platformText = CommandText.goldenArgs("info.platform", platform)
				.finish()
				.withStyle(style);

		this.versionText = CommandText.goldenArgs("info.version", version)
				.finish()
				.withStyle(style);

		this.minecraftVersionText = CommandText.goldenArgs("info.minecraft_version", minecraftVersion)
				.finish()
				.withStyle(style);
	}

	public static PatPatClientInfoCommand getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PatPatClientInfoCommand();
		}
		return INSTANCE;
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("info")
				.executes(context -> PatPatClientInfoCommand.getInstance().version(context));
	}

	public int version(CommandContext<FabricClientCommandSource> context) {
		context.sendMsg(this.platformText);
		context.sendMsg(this.minecraftVersionText);
		context.sendMsg(this.versionText);

		return Command.SINGLE_SUCCESS;
	}

}
