package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.network.PacketByteBuf;

import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.packet.BasePatPatPacket;
import net.lopymine.patpat.packet.PatPatPacketType;
import net.lopymine.patpat.utils.IdentifierUtils;

@Getter
public class HelloPatPatServerC2SPacket implements BasePatPatPacket<HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_server_c2s_packet";

	public static final PatPatPacketType<HelloPatPatServerC2SPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), HelloPatPatServerC2SPacket::new);

	private final Version version;

	public HelloPatPatServerC2SPacket() {
		this.version = Version.MOD_VERSION;
	}

	public HelloPatPatServerC2SPacket(PacketByteBuf buf) {
		int major = buf.readUnsignedByte();
		int minor = buf.readUnsignedByte();
		int patch = buf.readUnsignedByte();
		this.version = Version.of(major, minor, patch);
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeByte(this.version.major());
		buf.writeByte(this.version.minor());
		buf.writeByte(this.version.patch());
	}

	@Override
	public PatPatPacketType<HelloPatPatServerC2SPacket> getType() {
		return TYPE;
	}
}
