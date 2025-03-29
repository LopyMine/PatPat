package net.lopymine.patpat.manager.server.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.utils.CommandTextBuilder;

import java.util.Arrays;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class ListSetModeCommand {

	private ListSetModeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int setListMode(CommandContext<ServerCommandSource> context) {
		String modeId = StringArgumentType.getString(context, "mode");
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		ListMode listMode = ListMode.getById(modeId);
		if (config.getListMode() == listMode) {
			Text text = CommandTextBuilder.startBuilder("list.mode.already", listMode).build();
			context.getSource().sendPatPatFeedback(text, true);
			return 0;
		}

		boolean success = listMode != null;
		if (success) {
			config.setListMode(listMode);
			config.save();
		}

		String result = success ? "success" : "failed";
		String key = String.format("list.mode.%s", result);
		Object arg = success ? listMode.getText() : modeId;

		CommandTextBuilder builder = CommandTextBuilder.startBuilder(key, arg);
		if (!success) {
			builder.withHoverText(Arrays.stream(ListMode.values()).map(ListMode::getText).toArray());
		}
		Text text = builder.build();
		context.getSource().sendPatPatFeedback(text, true);
		if (success) {
			PatPat.LOGGER.info(text.asString());
		} else {
			PatPat.LOGGER.warn(text.asString());
		}
		return success ? Command.SINGLE_SUCCESS : 0;
	}
}
