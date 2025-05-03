package net.lopymine.patpat.packet.s2c;

import net.minecraft.network.*;

import net.lopymine.patpat.common.packet.PacketType;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

public class HelloPatPatPlayerS2CPacket implements BasePatPatPacket<HelloPatPatPlayerS2CPacket> {

	public static final String PACKET_ID = "hello_patpat_player_s2c_packet";

	public static final PacketType<HelloPatPatPlayerS2CPacket> TYPE = new PacketType<>(IdentifierUtils.id(PACKET_ID), HelloPatPatPlayerS2CPacket::new);

	public HelloPatPatPlayerS2CPacket() {
	}

	public HelloPatPatPlayerS2CPacket(PacketByteBuf buf) {
	}

	@Override
	public void write(PacketByteBuf buf) {
	}

	@Override
	public PacketType<HelloPatPatPlayerS2CPacket> getType() {
		return TYPE;
	}
}
