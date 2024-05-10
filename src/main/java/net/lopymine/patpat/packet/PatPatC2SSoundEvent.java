package net.lopymine.patpat.packet;

import net.minecraft.entity.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;

import java.util.UUID;

public class PatPatC2SSoundEvent implements FabricPacket {
    public static final PacketType<PatPatC2SSoundEvent> TYPE = PacketType.create(PatPat.i("patpat_c2s_sound_event"), PatPatC2SSoundEvent::new);

    private final int entityID;

    public PatPatC2SSoundEvent(Entity entity) {
        this.entityID = entity.getId();
    }

    public PatPatC2SSoundEvent(PacketByteBuf buf) {
        this.entityID = buf.readVarInt();
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
