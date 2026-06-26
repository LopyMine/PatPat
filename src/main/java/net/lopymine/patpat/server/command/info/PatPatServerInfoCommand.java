package net.lopymine.patpat.server.command.info;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entrypoint.EarlyCommonMultiLoader;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;

import static net.lopymine.patpat.server.command.PatPatServerCommandManager.permission;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerInfoCommand {

	private PatPatServerInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("info")
				.requires(permission("info"))
				.executes(PatPatServerInfoCommand::version);
	}

	public static int version(CommandContext<CommandSourceStack> context) {
		String platform = EarlyCommonMultiLoader.getInstance().getFullPlatform();

		String version = PatPat.MOD_VERSION + "+" + PatPat.BUILD_CODE_TIME;
		String minecraftVersion = SharedConstants.getCurrentVersion().name();
		String debugInformation = "Platform: %s%nMinecraft: %s%nVersion: %s"
				.formatted(platform, minecraftVersion, version);

		Style style = Style.EMPTY
				.withClickEvent(CommandText.getClickEvent(Action.COPY_TO_CLIPBOARD, debugInformation))
				.withHoverEvent(CommandText.getHoverEvent(HoverEvent.Action.SHOW_TEXT, CommandText.text("info.copy").finish()));

		MutableComponent platformText = CommandText.goldenArgs("info.platform", platform)
				.finish()
				.withStyle(style);

		MutableComponent versionText = CommandText.goldenArgs("info.version", version)
				.finish()
				.withStyle(style);

		MutableComponent minecraftVersionText = CommandText.goldenArgs("info.minecraft_version", minecraftVersion)
				.finish()
				.withStyle(style);

		context.sendMsg(platformText);
		context.sendMsg(minecraftVersionText);
		context.sendMsg(versionText);

		return Command.SINGLE_SUCCESS;
	}

}
