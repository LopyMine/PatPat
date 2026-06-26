package net.lopymine.patpat.packet;

import java.util.function.Function;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import java.util.function.Function;

@Getter
public class PatPatPacketType<T extends BasePatPatPacket<T>> {

	private final Function<FriendlyByteBuf, T> factory;
	private final Identifier id;
	private final Class<T> clazz;

	private final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<T> packetId;
	private final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, T> codec;

	public PatPatPacketType(Identifier id, Function<FriendlyByteBuf, T> factory, Class<T> clazz) {
		this.id = id;
		this.factory = factory;
		this.clazz = clazz;
		this.packetId = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(id);
		this.codec = net.minecraft.network.protocol.common.custom.CustomPacketPayload.codec(T::write, factory::apply);
	}

}
