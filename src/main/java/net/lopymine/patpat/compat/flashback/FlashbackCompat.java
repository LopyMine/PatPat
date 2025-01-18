package net.lopymine.patpat.compat.flashback;

import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.record.Recorder;
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
import net.lopymine.patpat.packet.PatEntityForReplayModS2CPacket;

import java.util.UUID;

public class FlashbackCompat {

	public static void onPat(UUID pattedEntity, UUID whoPatted) {
		//? flashback {
		PatPatClient.LOGGER.debug("Sending dummy packet for Flashback mod. [Patted: {} and Who: {}]", pattedEntity, whoPatted);
		if (!LoadedMods.FLASHBACK_MOD_LOADED) {
			PatPatClient.LOGGER.debug("Flashback not loaded");
			return;
		}

		Recorder recorder = Flashback.RECORDER;
		if (recorder != null) {
			PatEntityForReplayModS2CPacket patPacket = new PatEntityForReplayModS2CPacket(pattedEntity, whoPatted);
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