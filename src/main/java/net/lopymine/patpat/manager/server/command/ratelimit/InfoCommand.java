package net.lopymine.patpat.manager.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.CommandExtension;
import net.lopymine.patpat.manager.server.command.RateLimitManager;
import net.lopymine.patpat.utils.CommandTextBuilder;

import java.util.Collection;

@ExtensionMethod(CommandExtension.class)
public class InfoCommand {

	private InfoCommand() {
		throw new IllegalStateException("Command class");
	}


	public static int info(CommandContext<ServerCommandSource> context) {
		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		Text text = CommandTextBuilder.startBuilder("ratelimit.info",
				config.isEnabled(),
				config.getTokenLimit(),
				config.getTokenIncrement(),
				config.getTokenIncrementInterval().toString(),
				config.getPermissionBypass()
		).build();
		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int infoWithUser(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "profile");
		if (profiles.size() != 1) {
			Text text = CommandTextBuilder.startBuilder("ratelimit.info.only_one_player").build();
			context.getSource().sendPatPatFeedback(text);
		}

		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		GameProfile profile = profiles.iterator().next();
		context.getSource().sendPatPatFeedback("Info '%s'".formatted(profile.getName()), false);

		int availablePats = RateLimitManager.getAvailablePats(profile.getId());
		String message = String.valueOf(availablePats);
		if (context.getSource().hasPermission(config.getPermissionBypass(), 2)) {
			message = "bypass";
		}
		Text text = CommandTextBuilder.startBuilder("ratelimit.info.player", message).build();
		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}
}
