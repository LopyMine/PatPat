package net.lopymine.patpat.packet;

import org.jetbrains.annotations.Nullable;

//? if <=1.19.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
*///?}

public interface PongPatPacket<T extends PongPatPacket<T>> extends BasePatPatPacket<T> {

	void setPacketReply(PacketReply sender);

	@Nullable
	PacketReply getPacketReply();

	default boolean canPong() {
		return this.getPacketReply() != null;
	}

	//? if >=1.19.4 {
	default void pong(BasePatPatPacket<?> packet) {
		PacketReply reply = this.getPacketReply();
		if (reply == null) {
			return;
		}
		reply.reply(packet);
	}
	//?} else {
	/*default void pong(Identifier id, FriendlyByteBuf buf) {
		PacketReply reply = this.getPacketReply();
		if (reply == null) {
			return;
		}
		reply.reply(id, buf);
	}
	*///?}

}
