package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.network.*;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.packet.c2s.HelloPatPatServerC2SPacket;
import net.lopymine.patpat.utils.IdentifierUtils;

@Getter
public class HelloPatPatPlayerS2CPacket implements PingPatPacket<HelloPatPatPlayerS2CPacket, HelloPatPatServerC2SPacket> {

	public static final String PACKET_ID = "hello_patpat_player_s2c_packet";

	public static final PatPatPacketType<HelloPatPatPlayerS2CPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.modId(PACKET_ID), HelloPatPatPlayerS2CPacket::new);

	private final Version version;

	private PacketSender sender;

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
			PatPatClient.LOGGER.warn("Failed to parse server packet version from hello packet:", e);
			return Version.INVALID;
		}
	}

	@Override
	public HelloPatPatServerC2SPacket getPongPacket() {
		return new HelloPatPatServerC2SPacket().setPacketSender(this.sender);
	}

	@Override
	public void setPacketSender(PacketSender sender) {
		this.sender = sender;
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
