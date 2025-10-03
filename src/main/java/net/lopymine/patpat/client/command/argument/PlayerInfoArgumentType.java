package net.lopymine.patpat.client.command.argument;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.client.command.PatPatClientCommandManager;
import net.lopymine.patpat.extension.GameProfileExtension;
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
import net.lopymine.patpat.utils.CommandText;

import java.util.*;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(GameProfileExtension.class)
public class PlayerInfoArgumentType implements ArgumentType<PlayerInfo> {

	public static final DynamicCommandExceptionType FAILED_PARSING = new DynamicCommandExceptionType(o -> CommandText.text("error.failed_when_parsing", o).finish());
	public static final DynamicCommandExceptionType UNKNOWN_PLAYER = new DynamicCommandExceptionType(o -> CommandText.text("error.player_not_exist", o).finish());
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

			PatPatClientCommandManager.LOGGER.debug("Parsed PlayerInfo from PlayerInfoArgumentType: {}", s);

			ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
			if (networkHandler == null) {
				PatPatClientCommandManager.LOGGER.debug("networkHandler is null, cannot parse PlayerInfo from PlayerInfoArgumentType!");
				throw FAILED_PARSING.createWithContext(reader, reader.getString());
			}

			for (net.minecraft.client.multiplayer.PlayerInfo entry : networkHandler.getOnlinePlayers()) {
				GameProfile profile = entry.getProfile();
				if (profile.getName().equals(s)) {
					PlayerInfo playerInfo = new PlayerInfo(s, profile.getUUID());
					PatPatClientCommandManager.LOGGER.debug("Found PlayerInfo by nickname from PlayerInfoArgumentType, parsed: {}", playerInfo);
					return playerInfo;
				} else {
					UUID uuid = UUID.fromString(s);
					if (profile.getUUID().equals(uuid)) {
						PlayerInfo playerInfo = new PlayerInfo(profile.getName(), uuid);
						PatPatClientCommandManager.LOGGER.debug("Found PlayerInfo by uuid from PlayerInfoArgumentType, parsed: {}", playerInfo);
						return playerInfo;
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

	public record PlayerInfo(String nickname, UUID uuid) {

		@Override
		public String toString() {
			return "PlayerInfo{" +
					"nickname='" + this.nickname + '\'' +
					", uuid=" + this.uuid +
					'}';
		}
	}
}
