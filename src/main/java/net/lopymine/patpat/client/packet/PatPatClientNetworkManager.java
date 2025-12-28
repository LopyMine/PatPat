package net.lopymine.patpat.client.packet;

import net.lopymine.patpat.packet.*;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class PatPatClientNetworkManager {

	public static void sendPacketToServer(BasePatPatPacket<?> packet) {
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			ClientPacketDistributor.sendToServer(packet);
		}

		//	public static void sendPacketToServer(BasePatPatPacket<?> packet) {
		//		//? if <1.19.4 {
		//		/*ResourceLocation id = packet.getPatPatType().getId();
		//		FriendlyByteBuf buf = PacketByteBufs.create();
		//		packet.write(buf);
		//		*///?}
		//		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
		//			//? if >=1.19.4 {
		//			pingPong.pong(packet);
		//			//?} else {
		//			/*pingPong.pong(id, buf);
		//			*///?}
		//		} else {
		//			//? if >=1.19.4 {
		//			ClientPlayNetworking.send(packet);
		//			//?} else {
		//			/*ClientPlayNetworking.send(id, buf);
		//			*///?}
		//		}
		//	}
	}

}
