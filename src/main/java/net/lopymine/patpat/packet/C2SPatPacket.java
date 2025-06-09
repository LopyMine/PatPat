package net.lopymine.patpat.packet;

import net.minecraft.server.level.ServerLevel;

public interface C2SPatPacket<T extends BasePatPatPacket<T>> extends PatPacket<ServerLevel, T> {

}
