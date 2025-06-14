package net.lopymine.patpat.server.command.info;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerInfoCommand {

	public static final String PLATFORM = "Fabric/Server";

	private PatPatServerInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("info")
				.requires(context -> context.hasPatPatPermission("info"))
				.executes(PatPatServerInfoCommand::version);
	}

	public static int version(CommandContext<CommandSourceStack> context) {
		String version = PatPat.MOD_VERSION;
		String minecraftVersion = SharedConstants.getCurrentVersion().getName();
		String debugInformation = "Platform: %s Minecraft: %s Version: %s"
				.formatted(PLATFORM, minecraftVersion, version);

		Style style = Style.EMPTY
				.withClickEvent(CommandText.getClickEvent(Action.COPY_TO_CLIPBOARD, debugInformation))
				.withHoverEvent(CommandText.getHoverEvent(HoverEvent.Action.SHOW_TEXT, CommandText.text("info.copy").finish()));

		MutableComponent platformText = CommandText.goldenArgs("info.platform", PLATFORM)
				.finish()
				.withStyle(style);

		MutableComponent versionText = CommandText.goldenArgs("info.version", version)
				.finish()
				.withStyle(style);

		MutableComponent minecraftVersionText = CommandText.goldenArgs("info.minecraft_version", minecraftVersion)
				.finish()
				.withStyle(style);

		context.sendMsg(platformText, true);
		context.sendMsg(minecraftVersionText, true);
		context.sendMsg(versionText, true);

		return Command.SINGLE_SUCCESS;
	}

}
