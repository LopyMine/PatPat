package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;

import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityForReplayModS2CPacketV2 implements S2CPatPacket<PatEntityForReplayModS2CPacketV2> {

	public static final String PACKET_ID = "pat_entity_for_replay_s2c_packet_v2";

	public static final PatPatPacketType<PatEntityForReplayModS2CPacketV2> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), PatEntityForReplayModS2CPacketV2::new);

	private final int pattedEntityId;
	private final int whoPattedId;

	public PatEntityForReplayModS2CPacketV2(int pattedEntityId, int whoPattedId) {
		this.pattedEntityId = pattedEntityId;
		this.whoPattedId    = whoPattedId;
	}

	public PatEntityForReplayModS2CPacketV2(PacketByteBuf buf) {
		this.pattedEntityId = buf.readVarInt();
		this.whoPattedId    = buf.readVarInt();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeVarInt(this.pattedEntityId);
		buf.writeVarInt(this.whoPattedId);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ClientWorld world) {
		return world.getEntityById(this.getPattedEntityId());
	}

	@Override
	@Nullable
	public Entity getWhoPattedEntity(ClientWorld world) {
		return world.getEntityById(this.getWhoPattedId());
	}

	@Override
	public PatPatPacketType<PatEntityForReplayModS2CPacketV2> getType() {
		return TYPE;
	}
}
