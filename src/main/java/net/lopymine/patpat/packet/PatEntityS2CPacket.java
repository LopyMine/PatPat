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
	private final boolean donor;

	public PatEntityS2CPacket(UUID pattedEntityUuid, UUID playerUuid, String playerName, boolean donor) {
		this.pattedEntityUuid = pattedEntityUuid;
		this.playerUuid = playerUuid;
		this.playerName = playerName;
		this.donor = donor;
	}

	public PatEntityS2CPacket(PacketByteBuf buf) {
		this.pattedEntityUuid = buf.readUuid();
		this.playerUuid = buf.readUuid();
		this.playerName = buf.readString();
		this.donor = buf.readBoolean();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattedEntityUuid);
		buf.writeUuid(this.playerUuid);
		buf.writeString(this.playerName);
		buf.writeBoolean(this.donor);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
