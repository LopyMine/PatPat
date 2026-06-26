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
			Packet<?> packet = ServerPlayNetworking.createClientboundPacket(patPacket);
			recorder.writePacketAsync(
					packet,
					ConnectionProtocol.PLAY
			);
			PatPatClientPacketManager.LOGGER.debug("Saved packet to Flashback");
		}
		//?}
	}


}