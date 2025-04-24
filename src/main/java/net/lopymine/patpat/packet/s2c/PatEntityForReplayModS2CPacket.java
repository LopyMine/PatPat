package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityForReplayModS2CPacket implements S2CPatPacket {

	public static final String PACKET_ID = "pat_entity_for_replay_s2c_packet";

	//? >=1.20.5 {
	public static final Id<PatEntityForReplayModS2CPacket> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityForReplayModS2CPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityForReplayModS2CPacket::write, PatEntityForReplayModS2CPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityForReplayModS2CPacket> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), PatEntityForReplayModS2CPacket::new);
	 *///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	 *///?}

	private final UUID pattedEntityUuid;
	private final UUID whoPattedUuid;

	public PatEntityForReplayModS2CPacket(UUID pattedEntityUuid, UUID whoPattedUuid) {
		this.pattedEntityUuid = pattedEntityUuid;
		this.whoPattedUuid    = whoPattedUuid;
	}

	public PatEntityForReplayModS2CPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		this.pattedEntityUuid = buf.readUuid();
		this.whoPattedUuid    = buf.readUuid();
	}

	@Override
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
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
