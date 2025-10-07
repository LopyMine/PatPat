package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.common.command.list.PatPatCommonListChangeCommand;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.config.list.PatPatServerPlayerListConfig;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import java.util.*;

/*? if >=1.21.9 {*/
import net.minecraft.server.players.NameAndId;
/*?}*/

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
		Map<UUID, String> map = config.getValues();
		//? if >=1.21.9 {
		Collection<NameAndId> profile = GameProfileArgument.getGameProfiles(context, PROFILE_KEY);

		for (NameAndId nameAndId : profile) {
			String name = nameAndId.name();
			UUID uuid = nameAndId.id();
		//?} else {
		/*Collection<GameProfile> profile = GameProfileArgument.getGameProfiles(context, PROFILE_KEY);

		for (GameProfile gameProfile : profile) {
			String name = gameProfile.getName();
			UUID uuid = gameProfile.getId();
		*///?}
			PatPatCommonListChangeCommand.changeList(add, map, uuid, name, (component) -> context.sendMsg(component));
		}

		config.save();

		return Command.SINGLE_SUCCESS;
	}

}
