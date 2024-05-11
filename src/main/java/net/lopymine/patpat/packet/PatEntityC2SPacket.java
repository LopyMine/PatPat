package net.lopymine.patpat.packet;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.lopymine.patpat.utils.IdentifierUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class PatEntityC2SPacket implements FabricPacket {
	public static final PacketType<PatEntityC2SPacket> TYPE = PacketType.create(IdentifierUtils.id("pat_entity_c2s_packet"), PatEntityC2SPacket::new);

	private final UUID pattingPlayerUuid;
	private final UUID patEntityUuid;

	public PatEntityC2SPacket(PlayerEntity player, Entity entity) {
		this.pattingPlayerUuid = player.getUuid();
		this.patEntityUuid = entity.getUuid();
	}

	public PatEntityC2SPacket(PacketByteBuf buf) {
		this.pattingPlayerUuid = buf.readUuid(); // Проверить, правильно ли подставляются UUID's
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

	public UUID getPatEntityUuid() {
		return this.patEntityUuid;
	}

	public UUID getPattingPlayerUuid() {
		return this.pattingPlayerUuid;
	}
}
