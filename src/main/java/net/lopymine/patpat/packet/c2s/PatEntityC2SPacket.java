package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import net.minecraft.network.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Getter
public class PatEntityC2SPacket implements C2SPatPacket<PatEntityC2SPacket> {

	public static final String PACKET_ID = "pat_entity_c2s_packet";

	public static final PatPatPacketType<PatEntityC2SPacket> TYPE = new PatPatPacketType<>(IdentifierUtils.modId(PACKET_ID), PatEntityC2SPacket::new);

	private final UUID pattedEntityUuid;

	public PatEntityC2SPacket(Entity pattedEntity) {
		this.pattedEntityUuid = pattedEntity.getUUID();
	}

	public PatEntityC2SPacket(FriendlyByteBuf buf) {
		this.pattedEntityUuid = buf.readUUID();
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUUID(this.pattedEntityUuid);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ServerLevel world) {
		return world.getEntity(this.getPattedEntityUuid());
	}

	@Override
	public PatPatPacketType<PatEntityC2SPacket> getPatPatType() {
		return TYPE;
	}
}
