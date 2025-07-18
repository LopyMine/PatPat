package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.*;
import net.minecraft.world.entity.Entity;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;

import org.jetbrains.annotations.Nullable;

@Getter
public class SelfPatEntityS2CPacketV2 implements S2CPatPacket<SelfPatEntityS2CPacketV2> {

	public static final String PACKET_ID = "pat_entity_for_replay_s2c_packet_v2";

	public static final PatPatPacketType<SelfPatEntityS2CPacketV2> TYPE = new PatPatPacketType<>(IdentifierUtils.modId(PACKET_ID), SelfPatEntityS2CPacketV2::new);

	private final int pattedEntityId;
	private final int whoPattedId;

	public SelfPatEntityS2CPacketV2(int pattedEntityId, int whoPattedId) {
		this.pattedEntityId = pattedEntityId;
		this.whoPattedId    = whoPattedId;
	}

	public SelfPatEntityS2CPacketV2(FriendlyByteBuf buf) {
		this.pattedEntityId = buf.readVarInt();
		this.whoPattedId    = buf.readVarInt();
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(this.pattedEntityId);
		buf.writeVarInt(this.whoPattedId);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ClientLevel world) {
		return world.getEntity(this.getPattedEntityId());
	}

	@Override
	@Nullable
	public Entity getWhoPattedEntity(ClientLevel world) {
		return world.getEntity(this.getWhoPattedId());
	}

	@Override
	public PatPatPacketType<SelfPatEntityS2CPacketV2> getPatPatType() {
		return TYPE;
	}
}
