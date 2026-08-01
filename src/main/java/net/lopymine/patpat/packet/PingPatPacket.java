package net.lopymine.patpat.packet;

import org.jetbrains.annotations.Nullable;

public interface PingPatPacket<T extends PingPatPacket<T, A>, A extends PongPatPacket<A>> extends BasePatPatPacket<T> {

	void setPacketReply(PacketReply reply);

	@Nullable
	PacketReply getPacketReply();

	A getPongPacket();

}
