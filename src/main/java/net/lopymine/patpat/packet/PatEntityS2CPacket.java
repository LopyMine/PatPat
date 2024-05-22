package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.network.PacketByteBuf;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

@Getter
public class PatEntityS2CPacket implements FabricPacket {
	public static final PacketType<PatEntityS2CPacket> TYPE = PacketType.create(IdentifierUtils.id("pat_entity_s2c_packet"), PatEntityS2CPacket::new);

	private final UUID pattingPlayerUuid;
	private final UUID patEntityUuid;

	public PatEntityS2CPacket(UUID player, UUID entity) {
		this.pattingPlayerUuid = player;
		this.patEntityUuid = entity;
	}

	public PatEntityS2CPacket(PacketByteBuf buf) {
		this.pattingPlayerUuid = buf.readUuid();
		this.patEntityUuid = buf.readUuid();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattingPlayerUuid);
		buf.writeUuid(this.patEntityUuid);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
