package net.lopymine.patpat.manager.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.manager.server.command.RateLimitManager;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.CommandExtension;

import java.util.Collection;

@ExtensionMethod(CommandExtension.class)
public class InfoCommand {

	private InfoCommand() {
		throw new IllegalStateException("Command class");
	}


	public static int info(CommandContext<ServerCommandSource> context) {
		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		// TODO Сделать вывод более красивым + translatable
		context.getSource().sendPatPatFeedback("Enabled: " + config.isEnabled(), false);
		context.getSource().sendPatPatFeedback("Token limit: " + config.getTokenLimit(), false);
		context.getSource().sendPatPatFeedback("Token increment: " + config.getTokenIncrement(), false);
		context.getSource().sendPatPatFeedback("Token increment interval: " + config.getTokenIncrementInterval().toString(), false);
		context.getSource().sendPatPatFeedback("Permission bypass: " + config.getPermissionBypass(), false);

		return Command.SINGLE_SUCCESS;
	}

	public static int infoWithUser(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "profile");
		if (profiles.size() != 1) {
			context.getSource().sendPatPatFeedback("This command can have only one player in argument", false);
		}

		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		GameProfile profile = profiles.iterator().next();
		context.getSource().sendPatPatFeedback("Info '%s'".formatted(profile.getName()), false);

		int availablePats = RateLimitManager.getAvailablePats(profile.getId());
		String message = "Tokens: %d";
		if (context.getSource().hasPermission(config.getPermissionBypass(), 2)) {
			message = "Tokens: bypass";
		}

		context.getSource().sendPatPatFeedback(message.formatted(availablePats), false);
		return Command.SINGLE_SUCCESS;
	}
}
