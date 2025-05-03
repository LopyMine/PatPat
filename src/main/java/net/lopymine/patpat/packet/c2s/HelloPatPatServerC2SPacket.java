package net.lopymine.patpat.packet.c2s;

import net.minecraft.network.*;

import net.lopymine.patpat.common.packet.PacketType;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.packet.s2c.HelloPatPatPlayerS2CPacket;
import net.lopymine.patpat.utils.IdentifierUtils;

public class HelloPatPatServerC2SPacket implements BasePatPatPacket<HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_server_c2s_packet";

	public static final PacketType<HelloPatPatServerC2SPacket> TYPE = new PacketType<>(IdentifierUtils.id(PACKET_ID), HelloPatPatServerC2SPacket::new);

	public HelloPatPatServerC2SPacket() {
	}

	public HelloPatPatServerC2SPacket(PacketByteBuf buf) {
	}

	@Override
	public void write(PacketByteBuf buf) {
	}

	@Override
	public PacketType<HelloPatPatServerC2SPacket> getType() {
		return TYPE;
	}
}
