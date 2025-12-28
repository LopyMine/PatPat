package net.lopymine.patpat.packet.s2c;

import lombok.*;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.packet.c2s.HelloPatPatServerC2SPacket;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.network.FriendlyByteBuf;

@Getter
public class HelloPatPatPlayerS2CPacket implements PingPatPacket<HelloPatPatPlayerS2CPacket, HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_player_s2c_packet";

	public static final PatPatPacketType<HelloPatPatPlayerS2CPacket> TYPE = new PatPatPacketType<>(RLUtils.modId(PACKET_ID), HelloPatPatPlayerS2CPacket::new);

	private final Version version;

	@Setter
	private PacketReply packetReply;

	public HelloPatPatPlayerS2CPacket() {
		this.version = Version.CURRENT_MOD_VERSION;
	}

	public HelloPatPatPlayerS2CPacket(FriendlyByteBuf buf) {
		this.version = readVersion(buf);
	}

	private static Version readVersion(FriendlyByteBuf buf){
		try {
			return Version.readVersion(buf);
		} catch (Exception e) {
			PatPatClient.LOGGER.error("Failed to parse server packet version from hello packet:", e);
			return Version.INVALID;
		}
	}

	@Override
	public HelloPatPatServerC2SPacket getPongPacket() {
		HelloPatPatServerC2SPacket packet = new HelloPatPatServerC2SPacket();
		packet.setPacketReply(this.packetReply);
		return packet;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeByte(this.version.major());
		buf.writeByte(this.version.minor());
		buf.writeByte(this.version.patch());
	}

	@Override
	public PatPatPacketType<HelloPatPatPlayerS2CPacket> getPatPatType() {
		return TYPE;
	}
}
