package net.lopymine.patpat.packet.s2c;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import net.lopymine.patpat.packet.*;

import org.jetbrains.annotations.Nullable;

public interface S2CPatPacket<T extends BasePatPatPacket<T>> extends PatPacket<ClientWorld, T> {

	@Nullable Entity getWhoPattedEntity(ClientWorld world);

}
