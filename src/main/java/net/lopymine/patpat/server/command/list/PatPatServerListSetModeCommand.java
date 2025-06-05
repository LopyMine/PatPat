package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.utils.CommandTextBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import java.util.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerListSetModeCommand {

	private PatPatServerListSetModeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("set")
				.requires(context -> context.hasPatPatPermission("list.set"))
				.then(argument("mode", StringArgumentType.word())
						.suggests(((context, builder) -> SharedSuggestionProvider.suggest(List.of("WHITELIST", "BLACKLIST", "DISABLED"), builder)))
						.executes(PatPatServerListSetModeCommand::setListMode));
	}

	public static int setListMode(CommandContext<CommandSourceStack> context) {
		String modeId = StringArgumentType.getString(context, "mode");
		PatPatServerConfig config = PatPatServerConfig.getInstance();
		ListMode listMode = ListMode.getById(modeId);
		if (config.getListMode() == listMode) {
			Component text = CommandTextBuilder.startBuilder("list.mode.already", listMode).build();
			context.getSource().sendPatPatFeedback(text, true);
			return 0;
		}

		boolean success = listMode != null;
		if (success) {
			config.setListMode(listMode);
			config.saveAsync();
		}

		String result = success ? "success" : "failed";
		String key = String.format("list.mode.%s", result);
		Object arg = success ? listMode.getText() : modeId;

		CommandTextBuilder builder = CommandTextBuilder.startBuilder(key, arg);
		if (!success) {
			builder.withHoverText(Arrays.stream(ListMode.values()).map(ListMode::getText).toArray());
		}
		Component text = builder.build();
		context.getSource().sendPatPatFeedback(text, true);
		if (success) {
			PatPat.LOGGER.info(text.asString());
		} else {
			PatPat.LOGGER.warn(text.asString());
		}
		return success ? Command.SINGLE_SUCCESS : 0;
	}
}
