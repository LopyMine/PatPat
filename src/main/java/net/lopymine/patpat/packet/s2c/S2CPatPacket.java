package net.lopymine.patpat.packet.s2c;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import net.lopymine.patpat.packet.*;

import org.jetbrains.annotations.Nullable;

public interface S2CPatPacket extends PatPacket<ClientWorld> {

	@Nullable Entity getWhoPattedEntity(ClientWorld world);

}
