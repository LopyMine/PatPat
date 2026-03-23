package net.lopymine.patpat.compat.replaymod;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.client.packet.PatPatClientPacketManager;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.packet.s2c.*;

//? <=1.20.1 {
/*import net.minecraft.network.FriendlyByteBuf;
*///?}

public class ReplayModCompat {

	public static void onPat(int pattedEntityId, int whoPattedId) {
		//? if replaymod {
		/*if (!LoadedMods.REPLAY_MOD_LOADED) {
			return;
		}
		SelfPatEntityS2CPacketV2 packet = new SelfPatEntityS2CPacketV2(pattedEntityId, whoPattedId);
		//? >=1.20.2 {
		ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(packet));
		//?} else {
		/^FriendlyByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		ReplayModManager.sendDummyPacket(ServerPlayNetworking.createS2CPacket(packet.getPatPatType().getId(), buf));
		^///?}
		PatPatClientPacketManager.LOGGER.debug("Saved packet to ReplayMod");
		*///?}
	}
}
