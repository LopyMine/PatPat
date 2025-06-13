package net.lopymine.patpat.client.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.patpat.client.command.argument.ListModeArgumentType;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.ListMode;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.extension.ClientCommandExtension;
import net.lopymine.patpat.utils.*;

import java.util.*;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@ExtensionMethod(ClientCommandExtension.class)
public class PatPatClientListInfoCommand {

	public static final Style EMPTY_TEXT_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);

	private PatPatClientListInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<FabricClientCommandSource> get() {
		return literal("get")
				.executes(PatPatClientListInfoCommand::onInfo);
	}

	private static int onInfo(CommandContext<FabricClientCommandSource> context) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientServerConfig serverConfig = config.getServerConfig();

		MutableComponent component = TextUtils.literal("");
		Collection<String> values = PatPatClientPlayerListConfig.getInstance().getMap().values();
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
		Component text = CommandTextBuilder.startBuilder("list.info", serverConfig.getListMode().getText(), playersCount, component).build();

		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

}
