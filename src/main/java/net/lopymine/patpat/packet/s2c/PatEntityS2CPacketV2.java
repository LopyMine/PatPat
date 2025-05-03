package net.lopymine.patpat.packet.s2c;

import lombok.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.*;

import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.extension.EntityExtension;
import net.lopymine.patpat.utils.IdentifierUtils;

import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
@ExtensionMethod(EntityExtension.class)
public class PatEntityS2CPacketV2 implements S2CPatPacket<PatEntityS2CPacketV2> {

	public static final String PACKET_ID = "pat_entity_s2c_packet_v2";

	public static final PatPatPacketType<PatEntityS2CPacketV2> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), PatEntityS2CPacketV2::new);

	private final int pattedEntityId;
	private final int whoPattedId;

	public PatEntityS2CPacketV2(Entity pattedEntity, Entity whoPattedEntity) {
		this.pattedEntityId = pattedEntity.getEntityIntId();
		this.whoPattedId = whoPattedEntity.getEntityIntId();
	}

	public PatEntityS2CPacketV2(PacketByteBuf buf) {
		this.pattedEntityId = buf.readVarInt();
		this.whoPattedId = buf.readVarInt();
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
	public PatPatPacketType<PatEntityS2CPacketV2> getType() {
		return TYPE;
	}
}
