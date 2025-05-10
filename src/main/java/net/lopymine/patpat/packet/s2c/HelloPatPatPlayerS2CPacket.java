package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.network.*;

import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.packet.PatPatPacketType;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

@Getter
public class HelloPatPatPlayerS2CPacket implements BasePatPatPacket<HelloPatPatPlayerS2CPacket> {

	public static final String PACKET_ID = "hello_patpat_player_s2c_packet";

	public static final PatPatPacketType<HelloPatPatPlayerS2CPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), HelloPatPatPlayerS2CPacket::new);

	private final Version version;

	public HelloPatPatPlayerS2CPacket() {
		this.version = Version.MOD_VERSION;
	}

	public HelloPatPatPlayerS2CPacket(PacketByteBuf buf) {
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
	public PatPatPacketType<HelloPatPatPlayerS2CPacket> getType() {
		return TYPE;
	}
}
