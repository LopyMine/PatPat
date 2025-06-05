package net.lopymine.patpat.server.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lopymine.patpat.packet.*;
import net.minecraft.server.level.ServerPlayer;

//? <=1.19.3 {
/*import net.minecraft.network.PacketByteBuf;
 *///?}

public class PatPatServerNetworkManager {

	public static <T extends BasePatPatPacket<T>> void registerReceiver(PatPatPacketType<T> id, PacketListener<T> listener) {
		ServerPlayNetworking.registerGlobalReceiver(/*? if >=1.19.4 {*/ id.getPacketId(), /*?} else {*/ /*id.getId(), *//*?}*/
				/*? >=1.20.5 {*/(packet, context) -> { ServerPlayer sender = context.player(); /*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, sender, responseSender) -> {*//*?} else {*//*(server, sender, networkHandler, buf, responseSender) -> { T packet = id.getFactory().apply(buf); *//*?}*/
					listener.call(sender, packet);
				});
	}

	public static void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		//? >=1.19.4 {
		ServerPlayNetworking.send(player, packet);
		//?} else {
		/*try {
			Object o = packet.getClass().getField("PACKET_ID").get(null);
			if (!(o instanceof net.minecraft.util.Identifier id)) {
				throw new RuntimeException("Packet ID not Identifier");
			}
			PacketByteBuf buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
			packet.write(buf);
			ServerPlayNetworking.send(player, id, buf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*///?}
	}

	public interface PacketListener<T> {
		void call(ServerPlayer sender, T packet);
	}

}
