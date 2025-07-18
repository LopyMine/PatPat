package net.lopymine.patpat.packet;

// Every PatPat packets should extend/implement this interface
public interface BasePatPatPacket<T extends BasePatPatPacket<T>>
/*? >=1.20.5 {*/extends net.minecraft.network.protocol.common.custom.CustomPacketPayload
/*?} elif >=1.19.4 {*/ /*extends net.fabricmc.fabric.api.networking.v1.FabricPacket *//*?}*/ {

	void write(net.minecraft.network.FriendlyByteBuf buf);

	PatPatPacketType<T> getPatPatType();

	//? if >=1.20.5 {
	@Override
	default Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
		return this.getPatPatType().getPacketId();
	}
	//?} elif >=1.19.4 {
	/*@Override
	default net.fabricmc.fabric.api.networking.v1.PacketType<?> getType() {
		return this.getPatPatType().getPacketId();
	}
	*///?}
}
