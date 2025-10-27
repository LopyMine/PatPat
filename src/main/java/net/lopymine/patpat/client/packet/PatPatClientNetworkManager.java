package net.lopymine.patpat.client.packet;

/*? if <1.19.4 {*/
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
*//*?}*/

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.packet.*;

public class PatPatClientNetworkManager {

	public static <T extends BasePatPatPacket<T>> void registerReceiver(PatPatPacketType<T> id, PacketListener<T> listener) {
		ClientPlayNetworking.registerGlobalReceiver(/*? if >=1.19.4 {*/ id.getPacketId(), /*?} else {*//*id.getId(),*//*?}*/
				/*? >=1.20.5 {*/(packet, context) -> { PacketSender responseSender = context.responseSender();/*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, player, responseSender) -> {*//*?} else {*//*(client, handler, buf, responseSender) -> { T packet = id.getFactory().apply(buf); *//*?}*/
					if (packet instanceof PingPatPacket<?, ?> pingPacket) {
						pingPacket.setPacketSender(responseSender);
					}
					listener.call(packet);
				});
	}

	public static void sendPacketToServer(BasePatPatPacket<?> packet) {
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
			ClientPlayNetworking.send(packet);
			//?} else {
			/*ClientPlayNetworking.send(id, buf);
			*///?}
		}
	}

	public interface PacketListener<T> {
		void call(T packet);
	}

}
