package net.lopymine.patpat.packet;

import net.minecraft.network.*;
import net.minecraft.util.Identifier;

//? <=1.20.4 && >=1.19.4
/*import net.fabricmc.fabric.api.networking.v1.*;*/
//? >=1.20.5
import net.minecraft.network.packet.CustomPayload;
import net.lopymine.patpat.utils.IdentifierUtils;

public class HelloPatPatServerC2SPacket /*? >=1.20.5 {*/ implements CustomPayload /*?} elif >=1.19.4 {*/ /*implements FabricPacket *//*?}*/ {

	public static final Identifier PACKET_ID = IdentifierUtils.id("hello_patpat_server_c2s_packet");

	//? >=1.20.5 {
	public static final Id<HelloPatPatServerC2SPacket> TYPE = new Id<>(PACKET_ID);
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, HelloPatPatServerC2SPacket> CODEC = CustomPayload.codecOf(HelloPatPatServerC2SPacket::write, HelloPatPatServerC2SPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<HelloPatPatServerC2SPacket> TYPE = PacketType.create(PACKET_ID, HelloPatPatServerC2SPacket::new);
	*///?}

	public HelloPatPatServerC2SPacket() {
	}

	public HelloPatPatServerC2SPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
	}

	//? <=1.20.4 && >=1.19.4
	/*@Override*/
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
	}

	//? >=1.20.5 {
	@Override
	public Id<? extends CustomPayload> getId() {
		return TYPE;
	}
	//?} elif >=1.19.4 {
	/*@Override
	public PacketType<?> getType() {
		return TYPE;
	}
	*///?}
}
