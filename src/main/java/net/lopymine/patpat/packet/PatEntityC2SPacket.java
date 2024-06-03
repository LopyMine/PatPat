package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

@Getter
public class PatEntityC2SPacket implements FabricPacket {
	public static final PacketType<PatEntityC2SPacket> TYPE = PacketType.create(IdentifierUtils.id("pat_entity_c2s_packet"), PatEntityC2SPacket::new);

	private final UUID pattedEntityUuid;
	private final boolean donor;

	public PatEntityC2SPacket(Entity entity, boolean donor) {
		this.pattedEntityUuid = entity.getUuid();
		this.donor = donor;
	}

	public PatEntityC2SPacket(PacketByteBuf buf) {
		this.pattedEntityUuid = buf.readUuid();
		this.donor = buf.readBoolean();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattedEntityUuid);
		buf.writeBoolean(this.donor);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}
}
