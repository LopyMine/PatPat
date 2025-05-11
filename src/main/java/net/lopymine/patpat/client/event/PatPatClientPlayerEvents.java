package net.lopymine.patpat.client.event;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.packet.c2s.HelloPatPatServerC2SPacket;

public class PatPatClientPlayerEvents {

	public static void register() {
		C2SPlayChannelEvents.REGISTER.register((handler, sender, minecraft, channels) -> {
			PatPatClient.LOGGER.debug("[PING] Sending HelloPatPatServerC2S packet to the server...");
			PatPatClientNetworkManager.sendPacketToServer(new HelloPatPatServerC2SPacket());
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V1_VERSION);
			PatPatClient.LOGGER.debug("Disconnected, disabling v2 packets!!");
		});
	}

}
