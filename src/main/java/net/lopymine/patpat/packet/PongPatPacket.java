package net.lopymine.patpat.packet;

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
		PacketSender sender = this.getSender();
		if (sender == null) {
			return;
		}
		sender.sendPacket(packet);
	}
	//?} else {
	/*default void pong(net.minecraft.resources.ResourceLocation id, net.minecraft.network.FriendlyByteBuf buf) {
		PacketSender sender = this.getSender();
		if (sender == null) {
			return;
		}
		sender.sendPacket(id, buf);
	}
	*///?}

}
