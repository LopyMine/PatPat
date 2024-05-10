package net.lopymine.patpat.packet;

import net.minecraft.network.PacketByteBuf;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;

public class PatPatS2CSoundEvent implements FabricPacket {
    public static final PacketType<PatPatS2CSoundEvent> TYPE = PacketType.create(PatPat.i("patpat_s2c_sound_event"), PatPatS2CSoundEvent::new);
    private final int entityID;

    public PatPatS2CSoundEvent(PacketByteBuf buf) {
        this.entityID = buf.readVarInt();
    }

    public PatPatS2CSoundEvent(PatPatC2SSoundEvent packet) {
        this.entityID = packet.getEntityID();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityID);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public int getEntityID() {
        return this.entityID;
    }
}
