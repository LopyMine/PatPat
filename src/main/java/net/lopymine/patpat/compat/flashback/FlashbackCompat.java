package net.lopymine.patpat.compat.flashback;

//? >1.20.4 {
import net.minecraft.network.NetworkPhase;
//?} else {
/*import net.minecraft.network.*;
import net.minecraft.network.listener.ClientPlayPacketListener;
*///?}

//? >1.19.3 {
import net.minecraft.network.packet.Packet;
//?} else {
/*import net.minecraft.network.Packet;
*///?}

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.s2c.*;

//? flashback {
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.record.Recorder;
//?}


public class FlashbackCompat {

	public static void onPat(int pattedEntityId, int whoPattedId) {
		//? flashback {
		PatPatClient.LOGGER.debug("Sending dummy packet for Flashback mod. [Patted: {} and Who: {}]", pattedEntityId, whoPattedId);
		if (!LoadedMods.FLASHBACK_MOD_LOADED) {
			PatPatClient.LOGGER.debug("Flashback not loaded");
			return;
		}

		Recorder recorder = Flashback.RECORDER;
		if (recorder != null) {
			PatEntityForReplayModS2CPacketV2 patPacket = new PatEntityForReplayModS2CPacketV2(pattedEntityId, whoPattedId);
			//? >1.20.1 {
			Packet<?> packet = ServerPlayNetworking.createS2CPacket(patPacket);
			 //?} else {
			/*PacketByteBuf buf = PacketByteBufs.create();
			patPacket.write(buf);
			Packet<?> packet = ServerPlayNetworking.createS2CPacket(PatEntityForReplayModS2CPacket.PACKET_ID, buf);
			*///?}
			recorder.writePacketAsync(
					packet,
					/*? >1.20.4 {*/NetworkPhase.PLAY/*?} else {*//*NetworkState.PLAY*//*?}*/
			);
		}
		//?}
	}
}