package net.lopymine.patpat.packet;

import net.minecraft.network.RegistryByteBuf;

// Every PatPat packets should extend/implement this interface
public interface BasePatPatPacket extends /*? >=1.20.5 {*/ net.minecraft.network.packet.CustomPayload /*?} elif >=1.19.4 {*/ /* net.fabricmc.fabric.api.networking.v1.FabricPacket *//*?}*/ {

	void write(/*? if >=1.20.5 {*/RegistryByteBuf/*?} else {*//*PacketByteBuf*//*?}*/ buf);

}
