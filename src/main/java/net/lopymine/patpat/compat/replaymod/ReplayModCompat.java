package net.lopymine.patpat.compat.replaymod;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.s2c.*;

//? <=1.20.1 {
/*import net.minecraft.network.PacketByteBuf;
*///?}

public class ReplayModCompat {

	public static void onPat(int pattedEntityId, int whoPattedId) {
		PatPatClient.LOGGER.debug("Sending dummy packet for Replay Mod. [Patted: {} and Who: {}]", pattedEntityId, whoPattedId);
		if (!LoadedMods.REPLAY_MOD_LOADED) {
			PatPatClient.LOGGER.debug("Replay Mod not loaded");
			return;
		}
		PatEntityForReplayModS2CPacketV2 packet = new PatEntityForReplayModS2CPacketV2(pattedEntityId, whoPattedId);
		//? >=1.20.2 {
		ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(packet));
		//?} else {
		/*PacketByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(PatEntityForReplayModS2CPacket.PACKET_ID, buf));
		*///?}
	}
}
