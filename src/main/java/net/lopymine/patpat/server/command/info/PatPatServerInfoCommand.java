package net.lopymine.patpat.server.command.info;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.utils.*;

import net.minecraft.*;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;

import static net.minecraft.commands.Commands.literal;

@ExtensionMethod(CommandExtension.class)
public class PatPatServerInfoCommand {

	public static final String PLATFORM = "Fabric";

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
				.withClickEvent(CommandTextBuilder.getClickEvent(Action.COPY_TO_CLIPBOARD, debugInformation))
				.withHoverEvent(CommandTextBuilder.getHoverEvent(HoverEvent.Action.SHOW_TEXT, CommandTextBuilder.startBuilder("info.copy").build()));

		context.getSource().sendPatPatFeedback(
				CommandTextBuilder.startBuilder("info.platform", TextUtils.literal(PLATFORM).withStyle(ChatFormatting.GOLD))
						.build()
						.withStyle(style),
				true
		);

		context.getSource().sendPatPatFeedback(
				CommandTextBuilder.startBuilder("info.version", TextUtils.literal(version).withStyle(ChatFormatting.GOLD))
						.build()
						.withStyle(style),
				true
		);

		context.getSource().sendPatPatFeedback(
				CommandTextBuilder.startBuilder("info.minecraft_version", TextUtils.literal(minecraftVersion).withStyle(ChatFormatting.GOLD))
					.build()
					.withStyle(style),
				true
		);

		return Command.SINGLE_SUCCESS;
	}

}
