package net.lopymine.patpat.common.packet;

import lombok.Getter;
import net.minecraft.network.*;
import net.minecraft.util.Identifier;

import net.lopymine.patpat.packet.BasePatPatPacket;

import java.util.function.Function;

@Getter
public class PacketType<T extends BasePatPatPacket<T>> {

	private final Function<PacketByteBuf, T> factory;
	private final Identifier id;

	//? >=1.20.5 {
	private final net.minecraft.network.packet.CustomPayload.Id<T> packetId;
	private final net.minecraft.network.codec.PacketCodec<PacketByteBuf, T> codec;
	//?} elif >=1.19.4 {
	/*private final net.minecraft.network.PacketType<T> packetId;
	*///?}

	public PacketType(Identifier id, Function<PacketByteBuf, T> factory) {
		this.id = id;
		this.factory = factory;
		//? >=1.20.5 {
		this.packetId = new net.minecraft.network.packet.CustomPayload.Id<>(id);
		this.codec = net.minecraft.network.packet.CustomPayload.codecOf(T::write, factory::apply);
		//?} elif >=1.19.4 {
		/*this.packetId = net.minecraft.network.PacketType.create(id, factory);
		 *///?}
	}

}
