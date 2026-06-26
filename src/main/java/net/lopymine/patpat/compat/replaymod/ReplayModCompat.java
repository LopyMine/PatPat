package net.lopymine.patpat.compat.replaymod;

//? if fabric {
import net.fabricmc.fabric.api.networking.v1.*;
//?}

import net.lopymine.patpat.client.packet.PatPatClientPacketManager;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.s2c.*;


public class ReplayModCompat {

	public static void onPat(int pattedEntityId, int whoPattedId) {
		//? if replaymod {
		/*if (!LoadedMods.REPLAY_MOD_LOADED) {
			return;
		}
		SelfPatEntityS2CPacketV2 packet = new SelfPatEntityS2CPacketV2(pattedEntityId, whoPattedId);
		ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(packet));
		PatPatClientPacketManager.LOGGER.debug("Saved packet to ReplayMod");
		*///?}
	}
}
