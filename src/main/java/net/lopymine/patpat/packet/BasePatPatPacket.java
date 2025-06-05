package net.lopymine.patpat.packet;

// Every PatPat packets should extend/implement this interface
public interface BasePatPatPacket<T extends BasePatPatPacket<T>> /*? >=1.20.5 {*/ extends net.minecraft.network.protocol.common.custom.CustomPacketPayload {

	void write(net.minecraft.network.FriendlyByteBuf buf);

	PatPatPacketType<T> getType();

	//? if >=1.20.5 {
	@Override
	default Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
		return this.getType().getPacketId();
	}
	//?}
}
