package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.network.PacketByteBuf;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.packet.BasePatPatPacket;
import net.lopymine.patpat.packet.PatPatPacketType;
import net.lopymine.patpat.utils.IdentifierUtils;

@Getter
public class HelloPatPatServerC2SPacket implements BasePatPatPacket<HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_server_c2s_packet";

	public static final PatPatPacketType<HelloPatPatServerC2SPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), HelloPatPatServerC2SPacket::new);

	private final Version version;

	public HelloPatPatServerC2SPacket() {
		this.version = Version.CURRENT_MOD_VERSION;
	}

	public HelloPatPatServerC2SPacket(PacketByteBuf buf) {
		Version version = Version.INVALID;

		try {
			int major = buf.readUnsignedByte();
			int minor = buf.readUnsignedByte();
			int patch = buf.readUnsignedByte();
			version = new Version(major, minor, patch);
		} catch (Exception e) {
			PatPat.LOGGER.warn("Failed to parse client packet version from hello packet:", e);
		}

		this.version = version;
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
