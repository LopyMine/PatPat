package net.lopymine.patpat.packet;

import net.minecraft.network.PacketByteBuf;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;

import java.util.UUID;

public class PatEntityS2CPacket implements FabricPacket {
    public static final PacketType<PatEntityS2CPacket> TYPE = PacketType.create(PatPat.i("pat_entity_s2c_packet"), PatEntityS2CPacket::new);

    private final UUID pattingPlayerUuid;
    private final UUID patEntityUuid;

    public PatEntityS2CPacket(UUID player, UUID entity) {
        this.pattingPlayerUuid = player;
        this.patEntityUuid = entity;
    }

    public PatEntityS2CPacket(PacketByteBuf buf) {
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
