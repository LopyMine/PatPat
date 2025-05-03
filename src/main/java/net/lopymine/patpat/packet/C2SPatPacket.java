package net.lopymine.patpat.packet;

import net.minecraft.server.world.ServerWorld;

public interface C2SPatPacket<T extends BasePatPatPacket<T>> extends PatPacket<ServerWorld, T> {

}
