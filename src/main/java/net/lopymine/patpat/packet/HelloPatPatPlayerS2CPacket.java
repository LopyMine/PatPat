package net.lopymine.patpat.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

public class HelloPatPatPlayerS2CPacket /*? >=1.20.5 {*/implements CustomPayload /*?} elif >=1.19.4 {*/ /*implements FabricPacket *//*?}*/ {

	public static final Identifier PACKET_ID = IdentifierUtils.id("hello_patpat_player_s2c_packet");

	//? >=1.20.5 {
	public static final Id<HelloPatPatPlayerS2CPacket> TYPE = new Id<>(PACKET_ID);
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, HelloPatPatPlayerS2CPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(HelloPatPatPlayerS2CPacket::write, HelloPatPatPlayerS2CPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<HelloPatPatPlayerS2CPacket> TYPE = PacketType.create(PACKET_ID, HelloPatPatPlayerS2CPacket::new);
	 *///?}

	public HelloPatPatPlayerS2CPacket() {
	}

	public HelloPatPatPlayerS2CPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
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

