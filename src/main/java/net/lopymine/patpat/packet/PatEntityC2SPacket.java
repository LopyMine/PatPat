package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.utils.IdentifierUtils;

import java.util.UUID;

@Getter
public class PatEntityC2SPacket implements FabricPacket {
	public static final PacketType<PatEntityC2SPacket> TYPE = PacketType.create(IdentifierUtils.id("pat_entity_c2s_packet"), PatEntityC2SPacket::new);

	private final UUID pattingPlayerUuid;
	private final UUID patEntityUuid;

	public PatEntityC2SPacket(PlayerEntity player, Entity entity) {
		this.pattingPlayerUuid = player.getUuid();
		this.patEntityUuid = entity.getUuid();
	}

	public PatEntityC2SPacket(PacketByteBuf buf) {
		this.pattingPlayerUuid = buf.readUuid();
		this.patEntityUuid = buf.readUuid();
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeUuid(this.pattingPlayerUuid);
		buf.writeUuid(this.patEntityUuid);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
