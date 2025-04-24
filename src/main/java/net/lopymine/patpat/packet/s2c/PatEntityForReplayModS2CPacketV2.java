package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;

import net.lopymine.patpat.utils.*;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityForReplayModS2CPacketV2 implements S2CPatPacket {

	public static final String PACKET_ID = "pat_entity_for_replay_s2c_packet_v2";

	//? >=1.20.5 {
	public static final Id<PatEntityForReplayModS2CPacketV2> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityForReplayModS2CPacketV2> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityForReplayModS2CPacketV2::write, PatEntityForReplayModS2CPacketV2::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityForReplayModS2CPacket> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), PatEntityForReplayModS2CPacket::new);
	 *///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	 *///?}

	private final int pattedEntityId;
	private final int whoPattedId;

	public PatEntityForReplayModS2CPacketV2(int pattedEntityId, int whoPattedId) {
		this.pattedEntityId = pattedEntityId;
		this.whoPattedId    = whoPattedId;
	}

	public PatEntityForReplayModS2CPacketV2(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		this.pattedEntityId = buf.readVarInt();
		this.whoPattedId    = buf.readVarInt();
	}

	@Override
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
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

	//? >=1.20.5 {
	@Override
	public Id<? extends net.minecraft.network.packet.CustomPayload> getId() {
		return TYPE;
	}
	//?} elif >=1.19.4 {
	/*@Override
	public PacketType<?> getType() {
		return TYPE;
	}
	*///?}
}
