package net.lopymine.patpat.packet;

import net.minecraft.entity.Entity;

import org.jetbrains.annotations.Nullable;

public interface PatPacket<T> extends BasePatPatPacket {

	@Nullable Entity getPattedEntity(T world);

}
