package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.config.PatPatServerConfig;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.minecraft.commands.Commands.literal;


@ExtensionMethod(CommandExtension.class)
public class PatPatServerListInfoCommand {

	public static final Style EMPTY_TEXT_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);

	private PatPatServerListInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("get")
				.executes(PatPatServerListInfoCommand::onInfo);
	}

	private static int onInfo(CommandContext<CommandSourceStack> context) {
		PatPatServerConfig config = PatPatServerConfig.getInstance();

		MutableComponent component = TextUtils.literal("");
		Collection<String> values = PatPatServerPlayerListConfig.getInstance().getMap().values();
		int totalPlayers = values.size();
		Iterator<String> iterator = values.iterator();

		if (!iterator.hasNext()) {
			MutableComponent emptyText = CommandTextBuilder.startBuilder("list.empty")
					.build()
					.withStyle(EMPTY_TEXT_STYLE);
			component.append(emptyText);
		}

		int i = 1;
		while (iterator.hasNext()) {
			String value = iterator.next();
			component.append(i + ") ");
			component.append(TextUtils.literal(value + (iterator.hasNext() ? "\n" : "")).withStyle(ChatFormatting.GOLD));
			i++;
		}

		MutableComponent playersCount = TextUtils.literal(String.valueOf(totalPlayers)).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true));
		Component text = CommandTextBuilder.startBuilder("list.info", config.getListMode().getText(), playersCount, component).build();

		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

}
