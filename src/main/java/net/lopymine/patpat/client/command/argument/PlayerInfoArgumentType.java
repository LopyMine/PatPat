package net.lopymine.patpat.client.command.argument;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.util.Tuple;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;

import net.lopymine.patpat.client.command.argument.PlayerInfoArgumentType.PlayerInfo;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.CommandTextBuilder;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class PlayerInfoArgumentType implements ArgumentType<PlayerInfo> {

	public static final DynamicCommandExceptionType FAILED_PARSING = new DynamicCommandExceptionType(o -> CommandTextBuilder.startBuilder("argument.player_info.exception.failed_parsing", o).build());
	public static final DynamicCommandExceptionType UNKNOWN_PLAYER = new DynamicCommandExceptionType(o -> CommandTextBuilder.startBuilder("argument.player_info.exception.unknown_player", o).build());
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

			PatPatClient.LOGGER.debug("Parsed PlayerInfo from PlayerInfoArgumentType: {}", s);

			ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
			if (networkHandler == null) {
				throw FAILED_PARSING.createWithContext(reader, reader.getString());
			}

			for (net.minecraft.client.multiplayer.PlayerInfo entry : networkHandler.getOnlinePlayers()) {
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

		private final Tuple<String, UUID> pair;

		public PlayerInfo(String nickname, UUID uuid) {
			this.pair = new Tuple<>(nickname, uuid);
		}

		public String getNickname() {
			return this.pair.getA();
		}

		public UUID getUuid() {
			return this.pair.getB();
		}
	}
}
