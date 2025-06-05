package net.lopymine.patpat.client.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lopymine.patpat.packet.*;

//? <=1.19.3 {
/*import net.minecraft.network.PacketByteBuf;
 *///?}

public class PatPatClientNetworkManager {

	public static <T extends BasePatPatPacket<T>> void registerReceiver(PatPatPacketType<T> id, PacketListener<T> listener) {
		ClientPlayNetworking.registerGlobalReceiver(/*? if >=1.19.4 {*/ id.getPacketId(), /*?} else {*/ /*id.getId(), *//*?}*/
				/*? >=1.20.5 {*/(packet, context) -> {/*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, player, responseSender) -> {*//*?} else {*//*(client, handler, buf, responseSender) -> { T packet = id.getFactory().apply(buf); *//*?}*/
					listener.call(packet);
				});
	}

	public static void sendPacketToServer(BasePatPatPacket<?> packet) {
		//? >=1.19.4 {
		ClientPlayNetworking.send(packet);
		//?} else {
		/*try {
			Object o = packet.getClass().getField("PACKET_ID").get(null);
			if (!(o instanceof net.minecraft.util.Identifier id)) {
				throw new RuntimeException("Packet ID not Identifier");
			}
			PacketByteBuf buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
			packet.write(buf);
			ClientPlayNetworking.send(id, buf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*///?}
	}

	public interface PacketListener<T> {
		void call(T packet);
	}

}
