package net.lopymine.patpat.client.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.packet.*;

//? <=1.19.3 {
/*import net.minecraft.network.PacketByteBuf;
 *///?}

public class PatPatClientNetworkManager {

	public static <T extends BasePatPatPacket> void registerReceiver(
			/*? >=1.19.4 {*/
			net.minecraft.network.packet.CustomPayload.Id<T>
					/*?} else {*/
					/*net.minecraft.util.Identifier*/
					/*?}*/ id,
			PacketListener<T> listener
	) {
		ClientPlayNetworking.registerGlobalReceiver(id,
				/*? >=1.20.5 {*/(packet, context) -> {/*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, player, responseSender) -> {*//*?} else {*//*(client, handler, buf, responseSender) -> { PatEntityS2CPacket packet = new PatEntityS2CPacket(buf);*//*?}*/
					listener.call(packet);
				});
	}

	public static void sendPacketToServer(BasePatPatPacket packet) {
		//? >=1.19.4 {
		ClientPlayNetworking.send(packet);
		//?} else {
		/*try {
			Object o = packet.getClass().getField("PACKET_ID").get(null);
			if (!(o instanceof net.minecraft.util.Identifier id)) {
				throw new RuntimeException("Packet ID not Identifier");
			}
			PacketByteBuf buf = PacketByteBufs.create();
			packet.write(buf);
			ServerPlayNetworking.send(player, id, buf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*///?}
	}

	public interface PacketListener<T> {
		void call(T packet);
	}

}
