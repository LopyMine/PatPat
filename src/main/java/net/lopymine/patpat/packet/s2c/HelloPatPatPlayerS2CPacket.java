package net.lopymine.patpat.packet.s2c;

import net.minecraft.network.*;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

public class HelloPatPatPlayerS2CPacket implements BasePatPatPacket {

	public static final String PACKET_ID = "hello_patpat_player_s2c_packet";

	//? >=1.20.5 {
	public static final Id<HelloPatPatPlayerS2CPacket> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, HelloPatPatPlayerS2CPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(HelloPatPatPlayerS2CPacket::write, HelloPatPatPlayerS2CPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<HelloPatPatPlayerS2CPacket> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), HelloPatPatPlayerS2CPacket::new);
	 *///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	 *///?}

	public HelloPatPatPlayerS2CPacket() {
	}

	public HelloPatPatPlayerS2CPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
	}

	@Override
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
	}

	//? >=1.20.5 {
	@Override
	public Id<? extends net.minecraft.network.packet.CustomPayload> getId() {
		return TYPE;
	}
	//?} elif >=1.19.4 {
	/*@Override
	public PacketType<?> getType() {
		return TYPE;
	}
	*///?}
}
