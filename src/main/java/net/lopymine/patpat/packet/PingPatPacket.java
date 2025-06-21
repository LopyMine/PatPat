package net.lopymine.patpat.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import org.jetbrains.annotations.Nullable;

public interface PingPatPacket<T extends PingPatPacket<T, A>, A extends PongPatPacket<A>> extends BasePatPatPacket<T> {

	void setPacketSender(PacketSender sender);

	@Nullable
	PacketSender getSender();

	A getPongPacket();

}
