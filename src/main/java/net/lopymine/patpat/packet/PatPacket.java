package net.lopymine.patpat.packet;

import net.minecraft.entity.Entity;

import org.jetbrains.annotations.Nullable;

public interface PatPacket<W, T extends BasePatPatPacket<T>> extends BasePatPatPacket<T> {

	@Nullable Entity getPattedEntity(W world);

}
