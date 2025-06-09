package net.lopymine.patpat.client.event;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.common.Version;

public class PatPatClientPlayerEvents {

	private PatPatClientPlayerEvents() {
		throw new IllegalStateException("Events class");
	}

	public static void register() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V1_VERSION);
			PatPatClient.LOGGER.debug("Disconnected, disabling v2 packets!!");
		});
	}

}
