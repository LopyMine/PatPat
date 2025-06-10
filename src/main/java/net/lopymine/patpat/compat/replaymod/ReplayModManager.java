package net.lopymine.patpat.compat.replaymod;

import net.minecraft.network.protocol.Packet;
import net.lopymine.patpat.client.PatPatClient;

//? !(=1.20.5) {
import com.replaymod.recording.ReplayModRecording;
import com.replaymod.recording.packet.PacketListener;
import com.replaymod.replay.ReplayModReplay;
//?}

public class ReplayModManager {

	public static void sendDummyPacket(Packet<?> packet) {
		//? !(=1.20.5) {
		if (ReplayModRecording.instance.getConnectionEventHandler() != null) {
			PatPatClient.LOGGER.debug("Sent dummy packet for Replay Mod");
			PacketListener packetListener = ReplayModRecording.instance.getConnectionEventHandler().getPacketListener();
			if (packetListener != null) {
				packetListener.save(packet);
			}
		}
		//?}
	}

	public static boolean isInReplay() {
		//? !(=1.20.5) {
		ReplayModReplay instance = ReplayModReplay.instance;
		if (instance == null) {
			return false;
		}
		return instance.getReplayHandler() != null;
		//?} else {
		/*return false;
		 *///?}
	}
}
