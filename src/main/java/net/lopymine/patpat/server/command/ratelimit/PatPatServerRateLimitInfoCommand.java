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
import net.lopymine.patpat.*;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.HoverEvent.Action;

import java.util.Collection;
/*? if >=1.21.9 {*/
/*import net.minecraft.server.players.NameAndId;
*//*?}*/

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ExtensionMethod({CommandExtension.class, PlayerExtension.class, GameProfileExtension.class})
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

		Component statusComponent = PatTranslation.text("formatter.enabled_or_disabled." + config.isEnabled()).withStyle(ChatFormatting.GOLD);
		Object limitComponent = config.getTokenLimit();
		Object incrementComponent = config.getTokenIncrement();
		Object intervalComponent = config.getTokenIncrementInterval().toString();
		Object permissionComponent = TextUtils.literal(config.getPermissionBypass()).withStyle(
				Style.EMPTY.withHoverEvent(CommandText.getHoverEvent(
								Action.SHOW_TEXT,
								CommandText.text("ratelimit.info.permission_bypass.copy").finish()
						)).withClickEvent(CommandText.getClickEvent(
								ClickEvent.Action.COPY_TO_CLIPBOARD,
								config.getPermissionBypass()
						)).withColor(ChatFormatting.GOLD)
		);

		context.sendMsg(CommandText.goldenArgs("ratelimit.info.status", statusComponent).finish());
		context.sendMsg(CommandText.goldenArgs("ratelimit.info.limit", limitComponent).finish());
		context.sendMsg(CommandText.goldenArgs("ratelimit.info.increment", incrementComponent).finish());
		context.sendMsg(CommandText.goldenArgs("ratelimit.info.interval", intervalComponent).finish());
		context.sendMsg(CommandText.goldenArgs("ratelimit.info.permission_bypass", permissionComponent).finish());
		return Command.SINGLE_SUCCESS;
	}

	public static int infoWithUser(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		//? if >=1.21.9 {
		/*Collection<net.minecraft.server.players.NameAndId> profiles = GameProfileArgument.getGameProfiles(context, "profile");
		*///?} else {
		Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "profile");
		//?}
		if (profiles.size() != 1) {
			Component text = CommandText.text("error.only_one_player").finish();
			context.sendMsg(text);
		}

		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		//? if >=1.21.9 {
		/*net.minecraft.server.players.NameAndId profile = profiles.iterator().next();
		*///?} else {
		GameProfile profile = profiles.iterator().next();
		//?}
		if (!context.getSource().getOnlinePlayerNames().contains(profile.getName())) {
			Component text = CommandText.goldenArgs("error.player_not_exist", profile.getName()).finish();
			context.sendMsg(text);
			return 0;
		}

		int availablePats = PatPatServerRateLimitManager.getAvailablePats(profile.getUUID());
		//? if >=1.21.9 {
		/*sendInfo(context, profile, availablePats);
		*///?} else {
		profile.hasPermission(config.getPermissionBypass(), context).thenAcceptAsync(result -> {
			Object arg = result ?
					CommandText.text("ratelimit.info.tokens.bypass").finish().withStyle(ChatFormatting.GOLD)
					:
					availablePats;

			sendInfo(context, profile, arg);
		});
		//?}
		// TODO: add permissions api for >=1.21.9 when it will be updated to 1.21.9
		return Command.SINGLE_SUCCESS;
	}

	private static void sendInfo(CommandContext<CommandSourceStack> context, /*? if >=1.21.9 {*//*NameAndId*//*?} else {*/GameProfile/*?}*/ profile, Object tokens) {
		Component text = CommandText.goldenArgs("ratelimit.info.player", profile.getName()).finish();
		context.sendMsg(text);
		Component text2 = CommandText.goldenArgs("ratelimit.info.tokens", tokens).finish();
		context.sendMsg(text2);
	}
}
