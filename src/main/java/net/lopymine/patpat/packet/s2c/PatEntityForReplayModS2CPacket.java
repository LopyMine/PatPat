package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;

import net.lopymine.patpat.common.packet.PacketType;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityForReplayModS2CPacket implements S2CPatPacket<PatEntityForReplayModS2CPacket> {

	public static final String PACKET_ID = "pat_entity_for_replay_s2c_packet";

	public static final PacketType<PatEntityForReplayModS2CPacket> TYPE = new PacketType<>(IdentifierUtils.id(PACKET_ID), PatEntityForReplayModS2CPacket::new);

	private final UUID pattedEntityUuid;
	private final UUID whoPattedUuid;

	public PatEntityForReplayModS2CPacket(UUID pattedEntityUuid, UUID whoPattedUuid) {
		this.pattedEntityUuid = pattedEntityUuid;
		this.whoPattedUuid    = whoPattedUuid;
	}

	public PatEntityForReplayModS2CPacket(PacketByteBuf buf) {
		this.pattedEntityUuid = buf.readUuid();
		this.whoPattedUuid    = buf.readUuid();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattedEntityUuid);
		buf.writeUuid(this.whoPattedUuid);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ClientWorld world) {
		return WorldUtils.getEntity(world, this.getPattedEntityUuid());
	}

	@Override
	@Nullable
	public Entity getWhoPattedEntity(ClientWorld world) {
		return WorldUtils.getEntity(world, this.getPattedEntityUuid());
	}

	@Override
	public PacketType<PatEntityForReplayModS2CPacket> getType() {
		return TYPE;
	}
}
