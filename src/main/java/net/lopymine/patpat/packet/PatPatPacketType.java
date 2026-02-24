package net.lopymine.patpat.packet;

import java.util.function.Function;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;

@Getter
public class PatPatPacketType<T extends BasePatPatPacket<T>> {

	private final Function<FriendlyByteBuf, T> factory;
	private final ResourceLocation id;
	private final Class<T> clazz;

	//? >=1.20.5 {
	/*private final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<T> packetId;
	private final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, T> codec;
	*///?} elif >=1.19.4 && fabric {
	/*private final net.fabricmc.fabric.api.networking.v1.PacketType<T> packetId;
	*///?}

	public PatPatPacketType(ResourceLocation id, Function<FriendlyByteBuf, T> factory, Class<T> clazz) {
		this.id = id;
		this.factory = factory;
		this.clazz = clazz;
		//? >=1.20.5 {
		/*this.packetId = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(id);
		this.codec = net.minecraft.network.protocol.common.custom.CustomPacketPayload.codec(T::write, factory::apply);
		*///?} elif >=1.19.4 && fabric {
		/*this.packetId = net.fabricmc.fabric.api.networking.v1.PacketType.create(id, factory);
		 *///?}
	}

}
