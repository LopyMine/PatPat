package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;
import net.minecraft.server.world.ServerWorld;

import net.lopymine.patpat.packet.PatPatPacketType;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityC2SPacket implements C2SPatPacket<PatEntityC2SPacket> {

	public static final String PACKET_ID = "pat_entity_c2s_packet";

	public static final PatPatPacketType<PatEntityC2SPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), PatEntityC2SPacket::new);

	private final UUID pattedEntityUuid;

	public PatEntityC2SPacket(Entity pattedEntity) {
		this.pattedEntityUuid = pattedEntity.getUuid();
	}

	public PatEntityC2SPacket(PacketByteBuf buf) {
		this.pattedEntityUuid = buf.readUuid();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattedEntityUuid);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ServerWorld world) {
		return world.getEntity(this.getPattedEntityUuid());
	}

	@Override
	public PatPatPacketType<PatEntityC2SPacket> getType() {
		return TYPE;
	}
}
