package net.lopymine.patpat.server.packet;

import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.lopymine.patpat.packet.*;

//? <=1.19.3 {
/*import net.minecraft.network.PacketByteBuf;
 *///?}

public class PatPatServerNetworkManager {

	public static <T extends BasePatPatPacket> void registerReceiver(
			/*? >=1.19.4 {*/
			net.minecraft.network.packet.CustomPayload.Id<T>
					/*?} else {*/
					/*net.minecraft.util.Identifier*/
					/*?}*/ id,
			PacketListener<T> listener
	) {
		ServerPlayNetworking.registerGlobalReceiver(id,
				/*? >=1.20.5 {*/(packet, context) -> { ServerPlayerEntity sender = context.player(); /*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, sender, responseSender) -> {*//*?} else {*//*(server, sender, networkHandler, buf, responseSender) -> { PatEntityC2SPacket packet = new PatEntityC2SPacket(buf);*//*?}*/
					listener.call(sender, packet);
				});
	}

	public static void sendPacketToPlayer(ServerPlayerEntity player, BasePatPatPacket packet) {
		//? >=1.19.4 {
		ServerPlayNetworking.send(player, packet);
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
		void call(ServerPlayerEntity sender, T packet);
	}

}
