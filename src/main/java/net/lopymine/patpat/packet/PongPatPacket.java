package net.lopymine.patpat.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import org.jetbrains.annotations.Nullable;

public interface PongPatPacket<T extends PongPatPacket<T>> extends BasePatPatPacket<T> {

	T setPacketSender(PacketSender sender);

	@Nullable
	PacketSender getSender();

	default boolean canPong() {
		return this.getSender() != null;
	}

	//? if >=1.19.4 {
	default void pong(BasePatPatPacket<?> packet) {
		this.getSender().sendPacket(packet);
	}
	//?} else {
	/*default void pong(ResourceLocation id, FriendlyByteBuf buf) {
		PacketSender sender = this.getSender();
		if (sender == null) {
			return;
		}
		sender.sendPacket(id, buf);
	}
	*///?}

}
