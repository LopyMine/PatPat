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
public class PatEntityS2CPacket implements S2CPatPacket<PatEntityS2CPacket> {

	public static final String PACKET_ID = "pat_entity_s2c_packet";

	public static final PatPatPacketType<PatEntityS2CPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), PatEntityS2CPacket::new);

	private final UUID pattedEntityUuid;
	private final UUID whoPattedUuid;

	public PatEntityS2CPacket(Entity pattedEntity, Entity whoPattedEntity) {
		this.pattedEntityUuid = pattedEntity.getUuid();
		this.whoPattedUuid = whoPattedEntity.getUuid();
	}

	public PatEntityS2CPacket(PacketByteBuf buf) {
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
		return WorldUtils.getEntity(world, this.getWhoPattedUuid());
	}

	@Override
	public PatPatPacketType<PatEntityS2CPacket> getType() {
		return TYPE;
	}
}
