package net.lopymine.patpat.packet.c2s;

import net.minecraft.network.*;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

public class HelloPatPatServerC2SPacket implements BasePatPatPacket {

	public static final String PACKET_ID = "hello_patpat_server_c2s_packet";

	//? >=1.20.5 {
	public static final Id<HelloPatPatServerC2SPacket> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, HelloPatPatServerC2SPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(HelloPatPatServerC2SPacket::write, HelloPatPatServerC2SPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<HelloPatPatServerC2SPacket> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), HelloPatPatServerC2SPacket::new);
	*///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	*///?}

	public HelloPatPatServerC2SPacket() {
	}

	public HelloPatPatServerC2SPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
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
