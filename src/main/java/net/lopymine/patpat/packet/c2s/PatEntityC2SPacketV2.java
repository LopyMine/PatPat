package net.lopymine.patpat.packet.c2s;

import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import net.minecraft.network.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.lopymine.patpat.extension.EntityExtension;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.IdentifierUtils;

import org.jetbrains.annotations.Nullable;

@Getter
@ExtensionMethod(EntityExtension.class)
public class PatEntityC2SPacketV2 implements C2SPatPacket<PatEntityC2SPacketV2> {

	public static final String PACKET_ID = "pat_entity_c2s_packet_v2";

	public static final PatPatPacketType<PatEntityC2SPacketV2> TYPE = new PatPatPacketType<>(IdentifierUtils.id(PACKET_ID), PatEntityC2SPacketV2::new);

	private final int pattedEntityId;

	public PatEntityC2SPacketV2(Entity pattedEntity) {
		this.pattedEntityId = pattedEntity.getEntityIntId();
	}

	public PatEntityC2SPacketV2(FriendlyByteBuf buf) {
		this.pattedEntityId = buf.readVarInt();
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(this.pattedEntityId);
	}

	@Override
	@Nullable
	public Entity getPattedEntity(ServerLevel world) {
		return world.getEntity(this.getPattedEntityId());
	}

	@Override
	public PatPatPacketType<PatEntityC2SPacketV2> getType() {
		return TYPE;
	}
}
