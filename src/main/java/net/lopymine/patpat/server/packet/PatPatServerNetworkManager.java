package net.lopymine.patpat.server.packet;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.packet.*;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PatPatServerNetworkManager {

	public static <T extends BasePatPatPacket<T>> void registerReceiver(PatPatPacketType<T> id, PacketListener<T> listener) {
		ServerPlayNetworking.registerGlobalReceiver(/*? if >=1.19.4 {*/ id.getPacketId(), /*?} else {*/ /*id.getId(), *//*?}*/
				/*? >=1.20.5 {*/(packet, context) -> { ServerPlayer sender = context.player(); PacketSender responseSender = context.responseSender(); /*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, sender, responseSender) -> {*//*?} else {*//*(server, sender, networkHandler, buf, responseSender) -> { T packet = id.getFactory().apply(buf); *//*?}*/
					if (packet instanceof PingPatPacket<?, ?> pingPacket) {
						pingPacket.setPacketSender(responseSender);
					}
					listener.call(sender, packet);
				});
	}

	public static void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		//? if <1.19.4 {
		/*ResourceLocation id = packet.getPatPatType().getId();
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

	public interface PacketListener<T> {
		void call(ServerPlayer sender, T packet);
	}

}
