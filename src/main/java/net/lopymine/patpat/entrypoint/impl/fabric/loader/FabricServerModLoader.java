package net.lopymine.patpat.entrypoint.impl.fabric.loader;

//? if fabric {
import com.mojang.brigadier.CommandDispatcher;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
//? if <1.19 {
/*import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
*///?} else {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
 //?}
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

//? if >=1.21.9 {
import net.minecraft.server.players.NameAndId;
 //?} else {
/*import com.mojang.authlib.GameProfile;
*///?}

//? if <=1.19.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
*///?}

public class FabricServerModLoader implements IServerModLoader {

	@Override
	public void registerServerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
		//? if >=1.19 {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			consumer.accept(dispatcher);
		}));
		//?} else {
		/*CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
			consumer.accept(dispatcher);
		}));
		*///?}
	}

	@Override
	public void registerServerPlayerLogListener(ServerPlayerLogListener consumer) {
		ServerPlayConnectionEvents.INIT.register((handler, server) -> { ServerPlayer player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;
			consumer.onLog(true, player);
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> { ServerPlayer player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;
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
			//? if >=1.20.5 {
			if (registrationSide == PacketRegistrationSide.C2S || registrationSide == PacketRegistrationSide.BOTH) {
				//? if >=26.1 {
				var play = PayloadTypeRegistry.serverboundPlay();
				//?} else {
				/*var play = PayloadTypeRegistry.playC2S();
				 *///?}
				play.register(type.getPacketId(), type.getCodec());
			}
			if (registrationSide == PacketRegistrationSide.S2C || registrationSide == PacketRegistrationSide.BOTH) {
				//? if >=26.1 {
				var play = PayloadTypeRegistry.clientboundPlay();
				//?} else {
				/*var play = PayloadTypeRegistry.playS2C();
				 *///?}
				play.register(type.getPacketId(), type.getCodec());
			}
			//?}
			
			if (registrationSide != PacketRegistrationSide.C2S && registrationSide != PacketRegistrationSide.BOTH) {
				return;
			}

			ServerPlayNetworking.registerGlobalReceiver(/*? if >=1.19.4 {*/ type.getPacketId(), /*?} else {*/ /*type.getId(), *//*?}*/
				//? if >=1.20.5 {
				(packet, context) -> { ServerPlayer sender = context.player(); PacketSender responseSender = context.responseSender();
				//?} elif >=1.19.4 && <=1.20.4 {
				/*(packet, sender, responseSender) -> {
				 *///?} else {
				/*(server, sender, networkHandler, buf, responseSender) -> { P packet = type.getFactory().apply(buf);
				 *///?}
					if (packet instanceof PingPatPacket<?, ?> pingPacket) {
						pingPacket.setPacketReply(responseSender::sendPacket);
					}
					handler.handle(sender, packet);
				});
		}
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		//? if <1.19.4 {
		/*Identifier id = packet.getPatPatType().getId();
		FriendlyByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		*///?}
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			//? if >=1.19.4 {
			pingPong.pong(packet);
			//?} else {
			/*pingPong.pong(id, buf);
			 *///?}
		} else {
			//? if >=1.19.4 {
			ServerPlayNetworking.send(player, packet);
			//?} else {
			/*ServerPlayNetworking.send(player, id, buf);
			 *///?}
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
			/*? if >=1.21.9 {*/ NameAndId /*?} else {*/ /*GameProfile *//*?}*/ profile,
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
