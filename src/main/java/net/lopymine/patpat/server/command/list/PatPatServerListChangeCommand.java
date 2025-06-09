package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;
import net.lopymine.patpat.utils.CommandTextBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import java.util.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerListChangeCommand {

	public static final String PROFILE_KEY = "profile";

	private PatPatServerListChangeCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> getRemove() {
		return literal("remove")
				.requires(context -> context.hasPatPatPermission("list.remove"))
				.then(argument(PROFILE_KEY, GameProfileArgument.gameProfile())
						.suggests((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames(), builder))
						.executes(PatPatServerListChangeCommand::remove));
	}

	public static LiteralArgumentBuilder<CommandSourceStack> getAdd() {
		return literal("add")
				.requires(context -> context.hasPatPatPermission("list.add"))
				.then(argument(PROFILE_KEY, GameProfileArgument.gameProfile())
						.suggests(((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames(), builder)))
						.executes(PatPatServerListChangeCommand::add));
	}

	private static int remove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return onListChange(context, false);
	}

	private static int add(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return onListChange(context, true);
	}

	private static int onListChange(CommandContext<CommandSourceStack> context, boolean add) throws CommandSyntaxException {
		PatPatServerPlayerListConfig config = PatPatServerPlayerListConfig.getInstance();
		Map<UUID, String> map = config.getMap();
		Collection<GameProfile> profile = GameProfileArgument.getGameProfiles(context, PROFILE_KEY);

		for (GameProfile gameProfile : profile) {
			String name = gameProfile.getName();
			UUID uuid = gameProfile.getId();

			boolean success = add ? !map.containsKey(uuid) && map.put(uuid, name) == null : map.containsKey(uuid) && map.remove(uuid) != null;

			String action = add ? "add" : "remove";
			String result = success ? "success" : "failed";
			String key = String.format("list.%s.%s", action, result);

			Component text = CommandTextBuilder.startBuilder(key, gameProfile.getName())
					.withShowEntity(EntityType.PLAYER, uuid, gameProfile.getName())
					.withCopyToClipboard(uuid)
					.build();

			context.getSource().sendPatPatFeedback(text, true);
			if (success) {
				PatPat.LOGGER.info(text.asString());
			} else {
				PatPat.LOGGER.warn(text.asString());
			}
		}

		config.save();
		return Command.SINGLE_SUCCESS;
	}

}
