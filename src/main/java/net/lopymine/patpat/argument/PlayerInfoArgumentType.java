package net.lopymine.patpat.argument;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;

import net.lopymine.patpat.argument.PlayerInfoArgumentType.PlayerInfo;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class PlayerInfoArgumentType implements ArgumentType<PlayerInfo> {
	public static final DynamicCommandExceptionType FAILED_PARSING = new DynamicCommandExceptionType(o -> Text.stringifiedTranslatable("patpat.command.argument.player_info.exception.failed_parsing", o));
	public static final DynamicCommandExceptionType UNKNOWN_PLAYER = new DynamicCommandExceptionType(o -> Text.stringifiedTranslatable("patpat.command.argument.player_info.exception.unknown_player", o));
	private static final Collection<String> EXAMPLES = Arrays.asList("LopyMine", "nikita51", "192e3748-12d5-4573-a8a5-479cd394a1dc", "7b829ed5-9b74-428f-9b4d-ede06975fbc1");

	private PlayerInfoArgumentType() {
	}

	public static @NotNull PlayerInfoArgumentType player() {
		return new PlayerInfoArgumentType();
	}

	public static <S> PlayerInfo getPlayerInfo(String name, @NotNull CommandContext<S> context) {
		return context.getArgument(name, PlayerInfo.class);
	}

	@Override
	public PlayerInfo parse(@NotNull StringReader reader) throws CommandSyntaxException {
		try {
			String s = reader.readUnquotedString();

			ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
			if (networkHandler == null) {
				throw FAILED_PARSING.createWithContext(reader, reader.getString());
			}
			for (PlayerListEntry entry : networkHandler.getPlayerList()) {
				GameProfile profile = entry.getProfile();
				if (profile.getName().equals(s)) {
					return new PlayerInfo(s, profile.getId());
				} else {
					UUID uuid = UUID.fromString(s);
					if (profile.getId().equals(uuid)) {
						return new PlayerInfo(profile.getName(), uuid);
					}
				}
			}
			throw UNKNOWN_PLAYER.createWithContext(reader, reader.getString());
		} catch (CommandSyntaxException e) {
			throw FAILED_PARSING.createWithContext(reader, reader.getString());
		}
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static class PlayerInfo {
		private final Pair<String, UUID> pair;

		public PlayerInfo(String nickname, UUID uuid) {
			this.pair = new Pair<>(nickname, uuid);
		}

		public String getNickname() {
			return this.pair.getLeft();
		}

		public UUID getUuid() {
			return this.pair.getRight();
		}
	}
}
