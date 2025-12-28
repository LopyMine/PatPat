package net.lopymine.patpat.server.packet;

import net.lopymine.patpat.packet.*;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class PatPatServerNetworkManager {

	public static void sendPacketToPlayer(ServerPlayer player, BasePatPatPacket<?> packet) {
		if (packet instanceof PongPatPacket<?> pingPong && pingPong.canPong()) {
			pingPong.pong(packet);
		} else {
			PacketDistributor.sendToPlayer(player, packet);
		}
	}

}
