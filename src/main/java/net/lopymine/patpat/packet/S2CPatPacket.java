package net.lopymine.patpat.packet;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import org.jetbrains.annotations.Nullable;

public interface S2CPatPacket<T extends BasePatPatPacket<T>> extends PatPacket<ClientWorld, T> {

	@Nullable Entity getWhoPattedEntity(ClientWorld world);

}
