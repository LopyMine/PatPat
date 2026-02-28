package net.lopymine.patpat.entrypoint.loader.server;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.lopymine.patpat.packet.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IServerModLoader {

	void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer);

	void registerServerPlayerLogListener(ServerPlayerLogListener consumer);

	void registerOnServerStart(Runnable runnable);

	void registerOnServerStop(Runnable runnable);

	void registerServerPackets(Consumer<ServerPacketRegister> consumer);

	void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet);

	boolean hasPermission(ServerPlayer player, String permission);

	CompletableFuture<Boolean> hasOfflinePermission(UUID profile, String permission);

	void registerPermission(String permission);

	interface ServerPacketRegister {

		<P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler);

		enum PacketRegistrationSide {

			C2S,
			S2C,
			BOTH

		}

		interface PatPatServerPacketHandler<T extends BasePatPatPacket<T>> {

			void handle(ServerPlayer player, T packet);

		}

	}

	interface ServerPlayerLogListener {

		void onLog(boolean loggedIn, Player player);

	}

}
