package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

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
