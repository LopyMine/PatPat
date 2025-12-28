package net.lopymine.patpat.packet.c2s;

import lombok.*;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.network.FriendlyByteBuf;

@Getter
public class HelloPatPatServerC2SPacket implements PongPatPacket<HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_server_c2s_packet";

	public static final PatPatPacketType<HelloPatPatServerC2SPacket> TYPE = new PatPatPacketType<>(RLUtils.modId(PACKET_ID), HelloPatPatServerC2SPacket::new);

	private final Version version;

	@Setter
	private PacketReply packetReply;

	public HelloPatPatServerC2SPacket() {
		this.version = Version.CURRENT_MOD_VERSION;
	}

	public HelloPatPatServerC2SPacket(FriendlyByteBuf buf) {
		this.version = readVersion(buf);
	}

	private static Version readVersion(FriendlyByteBuf buf){
		try {
			return Version.readVersion(buf);
		} catch (Exception e) {
			PatPat.LOGGER.error("Failed to parse client packet version from hello packet:", e);
			return Version.INVALID;
		}
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeByte(this.version.major());
		buf.writeByte(this.version.minor());
		buf.writeByte(this.version.patch());
	}

	@Override
	public PatPatPacketType<HelloPatPatServerC2SPacket> getPatPatType() {
		return TYPE;
	}
}
