package net.lopymine.patpat.compat.replaymod;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.PatEntityForReplayModS2CPacket;

import java.util.UUID;

//? <=1.20.1 {
/*import net.minecraft.network.PacketByteBuf;
*///?}

public class ReplayModCompat {

	public static void onPat(UUID pattedEntity, UUID whoPatted) {
		if (PatPatClient.getConfig().isDebugLogEnabled()) {
			PatPatClient.info("Sending dummy packet for Replay Mod. [Patted: {} and Who: {}]", pattedEntity, whoPatted);
		}
		if (LoadedMods.REPLAY_MOD_LOADED) {
			PatEntityForReplayModS2CPacket packet = new PatEntityForReplayModS2CPacket(pattedEntity, whoPatted);
			//? >=1.20.2 {
			ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(packet));
			//?} else {
			/*PacketByteBuf buf = PacketByteBufs.create();
			packet.write(buf);
			ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(PatEntityForReplayModS2CPacket.PACKET_ID, buf));
			*///?}
			return;
		}
		if (PatPatClient.getConfig().isDebugLogEnabled()) {
			PatPatClient.info("Replay Mod not Installed!");
		}
	}
}
