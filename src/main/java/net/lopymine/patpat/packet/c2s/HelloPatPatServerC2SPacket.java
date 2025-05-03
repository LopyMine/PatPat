package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.network.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.common.packet.PacketType;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

@Getter
public class HelloPatPatServerC2SPacket implements BasePatPatPacket<HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_server_c2s_packet";

	public static final PacketType<HelloPatPatServerC2SPacket> TYPE = new PacketType<>(IdentifierUtils.id(PACKET_ID), HelloPatPatServerC2SPacket::new);

	private final Version version;

	public HelloPatPatServerC2SPacket() {
		this.version = Version.MOD_VERSION;
	}

	public HelloPatPatServerC2SPacket(PacketByteBuf buf) {
		this.version = Version.of(buf.readString());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(this.version.toString());
	}

	@Override
	public PacketType<HelloPatPatServerC2SPacket> getType() {
		return TYPE;
	}
}
