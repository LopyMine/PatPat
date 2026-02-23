package net.lopymine.patpat.packet;

import org.jetbrains.annotations.Nullable;

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
	/*default void pong(net.minecraft.resources.ResourceLocation id, net.minecraft.network.FriendlyByteBuf buf) {
		PacketSender sender = this.getSender();
		if (sender == null) {
			return;
		}
		sender.sendPacket(id, buf);
	}
	*///?}

}
