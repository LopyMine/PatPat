package net.lopymine.patpat.manager.server.command;

import lombok.experimental.ExtensionMethod;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.CommandExtenstion;

import java.util.Collection;

@ExtensionMethod(CommandExtenstion.class)
public class RateLimitCommand {

	private RateLimitCommand() {
		throw new IllegalStateException("Command class");
	}


	public static int info(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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
		Permissions.check(profile, config.getPermissionBypass()).thenAcceptAsync(result -> {
			String message = "Tokens: %d";
			if (Boolean.TRUE.equals(result)) {
				message = "Tokens: bypass";
			}
			context.getSource().sendPatPatFeedback(message.formatted(availablePats), false);
		});
		return Command.SINGLE_SUCCESS;
	}
}
