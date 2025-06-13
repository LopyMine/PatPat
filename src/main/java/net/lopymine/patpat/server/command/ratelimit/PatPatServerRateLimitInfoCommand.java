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
import net.lopymine.patpat.utils.*;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;

import java.util.Collection;
import java.util.function.Function;

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

		Component statusComponent = TextUtils.text("formatter.enabled_or_disabled." + config.isEnabled()).withStyle(ChatFormatting.GOLD);
		Component limitComponent = TextUtils.literal(config.getTokenLimit()).withStyle(ChatFormatting.GOLD);
		Component incrementComponent = TextUtils.literal(config.getTokenIncrement()).withStyle(ChatFormatting.GOLD);
		Component intervalComponent = TextUtils.literal(config.getTokenIncrementInterval().toString()).withStyle(ChatFormatting.GOLD);
		Component permissionComponent = TextUtils.literal(config.getPermissionBypass()).withStyle(
				Style.EMPTY.withClickEvent(CommandTextBuilder.getClickEvent(
								Action.COPY_TO_CLIPBOARD, config.getPermissionBypass()
						)).withHoverEvent(CommandTextBuilder.getHoverEvent(
								HoverEvent.Action.SHOW_TEXT,
								CommandTextBuilder.startBuilder("ratelimit.info.permission_bypass.copy").build()
						)).withColor(ChatFormatting.GOLD)
		);

		context.getSource().sendPatPatFeedback(CommandTextBuilder.startBuilder("ratelimit.info.status", statusComponent).build());
		context.getSource().sendPatPatFeedback(CommandTextBuilder.startBuilder("ratelimit.set.limit.info", limitComponent).build());
		context.getSource().sendPatPatFeedback(CommandTextBuilder.startBuilder("ratelimit.set.increment.info", incrementComponent).build());
		context.getSource().sendPatPatFeedback(CommandTextBuilder.startBuilder("ratelimit.set.interval.info", intervalComponent).build());
		context.getSource().sendPatPatFeedback(CommandTextBuilder.startBuilder("ratelimit.info.permission_bypass", permissionComponent).build());
		return Command.SINGLE_SUCCESS;
	}

	public static int infoWithUser(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "profile");
		if (profiles.size() != 1) {
			Component text = CommandTextBuilder.startBuilder("error.only_one_player").build();
			context.getSource().sendPatPatFeedback(text);
		}
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		GameProfile profile = profiles.iterator().next();
		if (!context.getSource().getOnlinePlayerNames().contains(profile.getName())) {
			Component text = CommandTextBuilder.startBuilder(
					"error.player_not_exist",
					TextUtils.literal(profile.getName()).withStyle(ChatFormatting.GOLD)
			).build();
			context.getSource().sendPatPatFeedback(text);
			return 0;
		}

		int availablePats = PatPatServerRateLimitManager.getAvailablePats(profile.getId());
		profile.hasPermission(config.getPermissionBypass(), context).thenAcceptAsync(result -> {
			MutableComponent message = (result ?
					CommandTextBuilder.startBuilder("ratelimit.info.tokens.bypass").build()
					:
					TextUtils.literal(availablePats)
			).withStyle(ChatFormatting.GOLD);

			Component text = CommandTextBuilder.startBuilder(
					"ratelimit.info.player",
					TextUtils.literal(profile.getName()).withStyle(ChatFormatting.GOLD)
			).build();

			Component text2 = CommandTextBuilder.startBuilder("ratelimit.info.tokens", message).build();
			context.getSource().sendPatPatFeedback(text);
			context.getSource().sendPatPatFeedback(text2);
		});
		return Command.SINGLE_SUCCESS;
	}
}
