package net.lopymine.patpat.packet;

//? if <=1.19.3 && !forge {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
*///?}

public interface PacketReply {

	//? if >=1.19.4 || forge {
	void reply(BasePatPatPacket<?> packet);
	//?} else {
	/*void reply(Identifier id, FriendlyByteBuf buf);
	 *///?}

}
