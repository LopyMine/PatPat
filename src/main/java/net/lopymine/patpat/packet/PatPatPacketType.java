package net.lopymine.patpat.packet;

import lombok.Getter;
import net.minecraft.network.*;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;

@Getter
public class PatPatPacketType<T extends BasePatPatPacket<T>> {

	private final Function<FriendlyByteBuf, T> factory;
	private final ResourceLocation id;

	//? >=1.20.5 {
	/*private final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<T> packetId;
	private final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, T> codec;
	*///?} elif >=1.19.4 {
	private final net.fabricmc.fabric.api.networking.v1.PacketType<T> packetId;
	//?}

	public PatPatPacketType(ResourceLocation id, Function<FriendlyByteBuf, T> factory) {
		this.id = id;
		this.factory = factory;
		//? >=1.20.5 {
		/*this.packetId = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(id);
		this.codec = net.minecraft.network.protocol.common.custom.CustomPacketPayload.codec(T::write, factory::apply);
		*///?} elif >=1.19.4 {
		this.packetId = net.fabricmc.fabric.api.networking.v1.PacketType.create(id, factory);
		 //?}
	}

}
