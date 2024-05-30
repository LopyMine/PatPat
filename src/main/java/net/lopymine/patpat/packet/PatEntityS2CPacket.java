package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.network.PacketByteBuf;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

@Getter
public class PatEntityS2CPacket implements FabricPacket {
	public static final PacketType<PatEntityS2CPacket> TYPE = PacketType.create(IdentifierUtils.id("pat_entity_s2c_packet"), PatEntityS2CPacket::new);

	private final UUID pattedEntityUuid;
	private final UUID playerUuid;
	private final String playerName;

	public PatEntityS2CPacket(UUID pattedEntityUuid, UUID playerUuid, String playerName) {
		this.pattedEntityUuid = pattedEntityUuid;
		this.playerUuid = playerUuid;
		this.playerName = playerName;
	}

	public PatEntityS2CPacket(PacketByteBuf buf) {
		this.pattedEntityUuid = buf.readUuid();
		this.playerUuid = buf.readUuid();
		this.playerName = buf.readString();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattedEntityUuid);
		buf.writeUuid(this.playerUuid);
		buf.writeString(this.playerName);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
