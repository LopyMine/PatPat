package net.lopymine.patpat.packet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface S2CPatPacket<T extends BasePatPatPacket<T>> extends PatPacket<ClientLevel, T> {

	@Nullable Entity getWhoPattedEntity(ClientLevel world);

}
