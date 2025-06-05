package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.lopymine.patpat.server.ratelimit.PatPatServerRateLimitManager;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.utils.CommandTextBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod({CommandExtension.class, PlayerExtension.class})
public class PatPatServerRateLimitInfoCommand {

	public static final String PROFILE_KEY = "profile";

	private PatPatServerRateLimitInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<CommandSourceStack> get() {
		return literal("info")
				.requires(context -> context.hasPatPatPermission("ratelimit.info"))
				.executes(PatPatServerRateLimitInfoCommand::info)
				.then(argument(PROFILE_KEY, GameProfileArgument.gameProfile())
						.suggests((context, builder) -> SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames(), builder))
						.executes(PatPatServerRateLimitInfoCommand::infoWithUser)
				);
	}

	public static int info(CommandContext<CommandSourceStack> context) {
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		Component text = CommandTextBuilder.startBuilder("ratelimit.info",
				config.isEnabled(),
				config.getTokenLimit(),
				config.getTokenIncrement(),
				config.getTokenIncrementInterval().toString(),
				config.getPermissionBypass()
		).build();
		context.getSource().sendPatPatFeedback(text);
		return Command.SINGLE_SUCCESS;
	}

	public static int infoWithUser(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "profile");
		if (profiles.size() != 1) {
			Component text = CommandTextBuilder.startBuilder("ratelimit.info.only_one_player").build();
			context.getSource().sendPatPatFeedback(text);
		}
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		GameProfile profile = profiles.iterator().next();
		if (!context.getSource().getOnlinePlayerNames().contains(profile.getName())) {
			Component text = CommandTextBuilder.startBuilder("ratelimit.info.player.not_online", profile.getName()).build();
			context.getSource().sendPatPatFeedback(text);
			return 0;
		}

		int availablePats = PatPatServerRateLimitManager.getAvailablePats(profile.getId());
		profile.hasPermission(config.getPermissionBypass(), context).thenAcceptAsync(result -> {
			String message = String.valueOf(availablePats);
			if (result) {
				message = "bypass";
			}
			Component text = CommandTextBuilder.startBuilder("ratelimit.info.player", profile.getName(), message).build();
			context.getSource().sendPatPatFeedback(text);
		});
		return Command.SINGLE_SUCCESS;
	}
}
