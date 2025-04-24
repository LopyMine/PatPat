package net.lopymine.patpat.packet.s2c;

import lombok.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;

import net.lopymine.patpat.utils.IdentifierUtils;

import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class PatEntityS2CPacketV2 implements S2CPatPacket {

	public static final String PACKET_ID = "pat_entity_s2c_packet_v2";

	//? >=1.20.5 {
	public static final Id<PatEntityS2CPacketV2> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityS2CPacketV2> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityS2CPacketV2::write, PatEntityS2CPacketV2::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityS2CPacketV2> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), PatEntityS2CPacketV2::new);
	 *///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	 *///?}

	private final int pattedEntityId;
	private final int whoPattedId;

	public PatEntityS2CPacketV2(Entity pattedEntity, Entity whoPattedEntity) {
		this.pattedEntityId = pattedEntity.getId();
		this.whoPattedId = whoPattedEntity.getId();
	}

	public PatEntityS2CPacketV2(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		this.pattedEntityId = buf.readVarInt();
		this.whoPattedId = buf.readVarInt();
	}

	@Override
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ClientWorld world) {
		return world.getEntityById(this.getPattedEntityId());
	}

	@Override
	@Nullable
	public Entity getWhoPattedEntity(ClientWorld world) {
		return world.getEntityById(this.getPattedEntityId());
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
