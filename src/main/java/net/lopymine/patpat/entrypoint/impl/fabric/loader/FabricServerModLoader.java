package net.lopymine.patpat.entrypoint.impl.fabric.loader;

//? if fabric {
import com.mojang.brigadier.CommandDispatcher;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.players.NameAndId;


public class FabricServerModLoader implements IServerModLoader {

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			consumer.accept(dispatcher);
		}));
	}

	@Override
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		ServerPlayConnectionEvents.INIT.register((handler, server) -> { ServerPlayer player =  handler.getPlayer() ;
			consumer.onLog(true, player);
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> { ServerPlayer player =  handler.getPlayer() ;
			consumer.onLog(false, player);
		});
	}

	@Override
	public void registerOnServerStart(Runnable runnable) {
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> runnable.run());
	}

	@Override
	public void registerOnServerStop(Runnable runnable) {
		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> runnable.run());
	}

	@Override
	public void registerServerPackets(Consumer<ServerPacketRegister> consumer) {
		consumer.accept(new FabricServerPacketRegister());
	}

	public static class FabricServerPacketRegister implements ServerPacketRegister {

		@Override
		public <P extends BasePatPatPacket<P>> void register(PatPatPacketType<P> type, PacketRegistrationSide registrationSide, PatPatServerPacketHandler<P> handler) {
			if (registrationSide == PacketRegistrationSide.C2S || registrationSide == PacketRegistrationSide.BOTH) {
				var play = PayloadTypeRegistry.serverboundPlay();
				play.register(type.getPacketId(), type.getCodec());
			}
			if (registrationSide == PacketRegistrationSide.S2C || registrationSide == PacketRegistrationSide.BOTH) {
				var play = PayloadTypeRegistry.clientboundPlay();
				play.register(type.getPacketId(), type.getCodec());
			}

			if (registrationSide != PacketRegistrationSide.C2S && registrationSide != PacketRegistrationSide.BOTH) {
				return;
			}

			ServerPlayNetworking.registerGlobalReceiver( type.getPacketId(),
				(packet, context) -> { ServerPlayer sender = context.player(); PacketSender responseSender = context.responseSender();
					if (packet instanceof PingPatPacket<?, ?> pingPacket) {
						pingPacket.setPacketReply(responseSender::sendPacket);
					}
					handler.handle(sender, packet);
				});
		}
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			ServerPlayNetworking.send(player, packet);
		}
	}

	@Override
	public boolean hasPermission(ServerPlayer player, String permission) {
		if (player == null) {
			return false;
		}
		return Permissions.check(player, permission, 2);
	}

	@Override
	public CompletableFuture<Boolean> hasOfflinePermission(
			 NameAndId  profile,
			MinecraftServer server,
			String permission
	) {
		return Permissions.check(profile, permission, 2, server);
	}

	@Override
	public void registerPermission(String permission) {
		// NO-OP
	}

}

//?}
