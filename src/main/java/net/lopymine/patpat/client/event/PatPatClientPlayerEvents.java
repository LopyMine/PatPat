package net.lopymine.patpat.client.event;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.packet.PatPatClientPacketManager;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;

public class PatPatClientPlayerEvents {

	private PatPatClientPlayerEvents() {
		throw new IllegalStateException("Events class");
	}

	public static void register() {
		ClientMultiLoader.getInstance().registerClientPlayerLogListener((loggingIn) -> {
			if (loggingIn) {
				return;
			}
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V1_VERSION);
			PatPatClient.LOGGER.debug("Disconnected from the server, reset current packets version to V1");
		});
	}

}
