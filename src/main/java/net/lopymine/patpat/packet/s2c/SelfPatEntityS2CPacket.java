package net.lopymine.patpat.packet.s2c;

import java.util.UUID;
import lombok.Getter;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Getter
public class SelfPatEntityS2CPacket implements S2CPatPacket<SelfPatEntityS2CPacket> {

	public static final String PACKET_ID = "pat_entity_for_replay_s2c_packet";

	public static final PatPatPacketType<SelfPatEntityS2CPacket> TYPE = new PatPatPacketType<>(RLUtils.modId(PACKET_ID), SelfPatEntityS2CPacket::new, SelfPatEntityS2CPacket.class);

	private final UUID pattedEntityUuid;
	private final UUID whoPattedUuid;

	public SelfPatEntityS2CPacket(UUID pattedEntityUuid, UUID whoPattedUuid) {
		this.pattedEntityUuid = pattedEntityUuid;
		this.whoPattedUuid    = whoPattedUuid;
	}

	public SelfPatEntityS2CPacket(FriendlyByteBuf buf) {
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
		return WorldUtils.getEntity(world, this.getPattedEntityUuid());
	}

	@Override
	public PatPatPacketType<SelfPatEntityS2CPacket> getPatPatType() {
		return TYPE;
	}
}
