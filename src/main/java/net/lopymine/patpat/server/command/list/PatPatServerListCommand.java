package net.lopymine.patpat.server.command.list;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.server.config.sub.PatPatServerPlayerListConfig;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.utils.CommandTextBuilder;

import java.util.*;

import static net.lopymine.patpat.server.command.PatPatServerCommandManager.PROFILE_NAME;

@ExtensionMethod({TextExtension.class, CommandExtension.class})
public class PatPatServerListCommand {

	private PatPatServerListCommand() {
		throw new IllegalStateException("Command class");
	}

	public static int remove(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return onListChange(context, false);
	}

	public static int add(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return onListChange(context, true);
	}

	private static int onListChange(CommandContext<ServerCommandSource> context, boolean add) throws CommandSyntaxException {
		PatPatServerPlayerListConfig config = PatPatServerPlayerListConfig.getInstance();
		Set<UUID> uuids = config.getUuids();
		Collection<GameProfile> profile = GameProfileArgumentType.getProfileArgument(context, PROFILE_NAME);
		String action = add ? "add" : "remove";
		boolean modify = false;
		for (GameProfile gameProfile : profile) {
			UUID uuid = gameProfile.getId();
			boolean success = false;
			if (add && !uuids.contains(uuid)) {
				uuids.add(uuid);
				success = true;
				modify  = true;
			} else if (!add && uuids.contains(uuid)) {
				uuids.remove(uuid);
				success = true;
				modify  = true;
			}

			String result = success ? "success" : "failed";
			String key = String.format("list.%s.%s", action, result);
			Text text = CommandTextBuilder.startBuilder(key, gameProfile.getName())
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
		if (modify) {
			config.save();
		}
		return Command.SINGLE_SUCCESS;
	}

}
