package net.lopymine.patpat.packet.s2c;

import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.*;
import net.minecraft.world.entity.Entity;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityS2CPacket implements S2CPatPacket<PatEntityS2CPacket> {

	public static final String PACKET_ID = "pat_entity_s2c_packet";

	public static final PatPatPacketType<PatEntityS2CPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.modId(PACKET_ID), PatEntityS2CPacket::new);

	private final UUID pattedEntityUuid;
	private final UUID whoPattedUuid;

	public PatEntityS2CPacket(Entity pattedEntity, Entity whoPattedEntity) {
		this.pattedEntityUuid = pattedEntity.getUUID();
		this.whoPattedUuid = whoPattedEntity.getUUID();
	}

	public PatEntityS2CPacket(FriendlyByteBuf buf) {
		this.pattedEntityUuid = buf.readUUID();
		this.whoPattedUuid    = buf.readUUID();
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(this.pattedEntityUuid);
		buf.writeUUID(this.whoPattedUuid);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ClientLevel world) {
		return WorldUtils.getEntity(world, this.getPattedEntityUuid());
	}

	@Override
	@Nullable
	public Entity getWhoPattedEntity(ClientLevel world) {
		return WorldUtils.getEntity(world, this.getWhoPattedUuid());
	}

	@Override
	public PatPatPacketType<PatEntityS2CPacket> getPatPatType() {
		return TYPE;
	}
}
