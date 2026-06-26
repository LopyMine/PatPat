package net.lopymine.patpat.packet;

import org.jetbrains.annotations.Nullable;


public interface PongPatPacket<T extends PongPatPacket<T>> extends BasePatPatPacket<T> {

	void setPacketReply(PacketReply sender);

	@Nullable
	PacketReply getPacketReply();

	default boolean canPong() {
		return this.getPacketReply() != null;
	}

	default void pong(BasePatPatPacket<?> packet) {
		PacketReply reply = this.getPacketReply();
		if (reply == null) {
			return;
		}
		reply.reply(packet);
	}

}
