package net.lopymine.patpat.server.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

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

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@ExtensionMethod({CommandExtension.class, PlayerExtension.class})
public class PatPatServerRateLimitInfoCommand {

	public static final String PROFILE_KEY = "profile";

	private PatPatServerRateLimitInfoCommand() {
		throw new IllegalStateException("Command class");
	}

	public static LiteralArgumentBuilder<ServerCommandSource> get() {
		return literal("info")
				.requires(context -> context.hasPatPatPermission("ratelimit.info"))
				.executes(PatPatServerRateLimitInfoCommand::info)
				.then(argument(PROFILE_KEY, GameProfileArgumentType.gameProfile())
						.suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
						.executes(PatPatServerRateLimitInfoCommand::infoWithUser)
				);
	}

	public static int info(CommandContext<ServerCommandSource> context) {
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
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
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		GameProfile profile = profiles.iterator().next();
		if (!context.getSource().getPlayerNames().contains(profile.getName())) {
			Text text = CommandTextBuilder.startBuilder("ratelimit.info.player.not_online", profile.getName()).build();
			context.getSource().sendPatPatFeedback(text);
			return 0;
		}

		int availablePats = PatPatServerRateLimitManager.getAvailablePats(profile.getId());
		profile.hasPermission(config.getPermissionBypass(), context).thenAcceptAsync(result -> {
			String message = String.valueOf(availablePats);
			if (result) {
				message = "bypass";
			}
			Text text = CommandTextBuilder.startBuilder("ratelimit.info.player", profile.getName(), message).build();
			context.getSource().sendPatPatFeedback(text);
		});
		return Command.SINGLE_SUCCESS;
	}
}
