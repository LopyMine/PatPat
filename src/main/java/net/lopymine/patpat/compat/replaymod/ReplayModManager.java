package net.lopymine.patpat.compat.replaymod;

import net.lopymine.patpat.client.PatPatClient;

//? >=1.19.4 {
import net.minecraft.network.packet.Packet;
//?} else {
/*import net.minecraft.network.Packet;
 *///?}

/**
 * !!--------------------------!!
 * <p>
 * YOU CAN'T INVOKE THIS CLASS OR THEIR METHODS DIRECTLY, MOD WILL CRASH AT PROD
 * <p>
 * YOU ONLY CAN USE {@link ReplayModCompat} TO INVOKE METHODS OF THIS CLASS
 * <p>
 * !!--------------------------!!
 */
public class ReplayModManager {

	public static void sendDummyPacket(Packet<?> packet) {
		//? !(=1.20.5) {
		if (com.replaymod.recording.ReplayModRecording.instance.getConnectionEventHandler() != null) {
			if (PatPatClient.getConfig().isDebugLogEnabled()) {
				PatPatClient.info("Sent dummy packet for Replay Mod");
			}
			com.replaymod.recording.ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().save(packet);
		}
		//?}
	}
}
