package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;
import net.minecraft.server.world.ServerWorld;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityC2SPacketV2 implements PatPacket<ServerWorld> {

	public static final String PACKET_ID = "pat_entity_c2s_packet_v2";

	//? >=1.20.5 {
	public static final Id<PatEntityC2SPacketV2> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityC2SPacketV2> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityC2SPacketV2::write, PatEntityC2SPacketV2::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityC2SPacketV2> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), PatEntityC2SPacketV2::new);
	 *///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	 *///?}

	private final int pattedEntityId;

	public PatEntityC2SPacketV2(Entity pattedEntity) {
		this.pattedEntityId = pattedEntity.getId();
	}

	public PatEntityC2SPacketV2(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		this.pattedEntityId = buf.readVarInt();
	}

	@Override
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ServerWorld world) {
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
