package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;
import net.minecraft.server.world.ServerWorld;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityC2SPacket implements PatPacket<ServerWorld> {

	public static final String PACKET_ID = "pat_entity_c2s_packet";

	//? >=1.20.5 {
	public static final Id<PatEntityC2SPacket> TYPE = new Id<>(IdentifierUtils.id(PACKET_ID));
	public static final net.minecraft.network.codec.PacketCodec<RegistryByteBuf, PatEntityC2SPacket> CODEC = net.minecraft.network.packet.CustomPayload.codecOf(PatEntityC2SPacket::write, PatEntityC2SPacket::new);
	//?} elif >=1.19.4 {
	/*public static final PacketType<PatEntityC2SPacket> TYPE = PacketType.create(IdentifierUtils.id(PACKET_ID), PatEntityC2SPacket::new);
	 *///?} else {
	/*public static final net.minecraft.util.Identifier TYPE = IdentifierUtils.id(PACKET_ID);
	 *///?}

	private final UUID pattedEntityUuid;

	public PatEntityC2SPacket(Entity pattedEntity) {
		this.pattedEntityUuid = pattedEntity.getUuid();
	}

	public PatEntityC2SPacket(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		this.pattedEntityUuid = buf.readUuid();
	}

	@Override
	public void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf) {
		buf.writeUuid(this.pattedEntityUuid);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ServerWorld world) {
		return world.getEntity(this.getPattedEntityUuid());
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
