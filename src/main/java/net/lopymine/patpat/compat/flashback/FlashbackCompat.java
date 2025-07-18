package net.lopymine.patpat.compat.flashback;

//? flashback {
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.record.Recorder;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.s2c.*;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.lopymine.patpat.client.packet.PatPatClientPacketManager;
//?}

public class FlashbackCompat {

	public static void onPat(int pattedEntityId, int whoPattedId) {
		//? flashback {
		if (!LoadedMods.FLASHBACK_MOD_LOADED) {
			return;
		}

		Recorder recorder = Flashback.RECORDER;
		if (recorder != null) {
			SelfPatEntityS2CPacketV2 patPacket = new SelfPatEntityS2CPacketV2(pattedEntityId, whoPattedId);
			//? >1.20.1 {
			Packet<?> packet = ServerPlayNetworking.createS2CPacket(patPacket);
			 //?} else {
			/*PacketByteBuf buf = PacketByteBufs.create();
			patPacket.write(buf);
			Packet<?> packet = ServerPlayNetworking.createS2CPacket(SelfPatEntityS2CPacket.PACKET_ID, buf);
			*///?}
			recorder.writePacketAsync(
					packet,
					/*? >1.20.4 {*/ConnectionProtocol.PLAY/*?} else {*//*NetworkState.PLAY*//*?}*/
			);
			PatPatClientPacketManager.LOGGER.debug("Saved packet to Flashback");
		}
		//?}
	}


}