package net.lopymine.patpat.packet;

// Every PatPat packets should extend/implement this interface
public interface BasePatPatPacket<T extends BasePatPatPacket<T>> /*? >=1.20.5 {*/ extends net.minecraft.network.packet.CustomPayload /*?} elif >=1.19.4 {*/ /* extends net.fabricmc.fabric.api.networking.v1.FabricPacket *//*?}*/ {

	void write(net.minecraft.network.PacketByteBuf buf);

	PatPatPacketType<T> getType();

	//? if >=1.20.5 {
	@Override
	default Id<? extends net.minecraft.network.packet.CustomPayload> getId() {
		return this.getType().getPacketId();
	}
	//?}
}
