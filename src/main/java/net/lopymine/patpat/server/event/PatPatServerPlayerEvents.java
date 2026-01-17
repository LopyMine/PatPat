package net.lopymine.patpat.server.event;

import net.lopymine.patpat.*;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.entrypoint.MultiLoader;
import net.lopymine.patpat.packet.s2c.HelloPatPatPlayerS2CPacket;
import net.lopymine.patpat.server.packet.PatPatServerPacketManager;

import net.minecraft.server.level.ServerPlayer;

public class PatPatServerPlayerEvents {

	private PatPatServerPlayerEvents() {
		throw new IllegalStateException("Events class");
	}

	public static void register() {
		MultiLoader.getInstance().registerServerPlayerLogListener((loggedIn, player) -> {
			if (loggedIn) {
				if(!(player instanceof ServerPlayer serverPlayer)){
					PatPat.LOGGER.warn("Not instance of ServerPlayer");
					return;
				}
				PatPatServerPacketManager.PLAYER_VERSIONS.put(player.getUUID(), Version.PACKET_V1_VERSION);
				PatPat.LOGGER.debug("Player {} just joined, sending hello packet...", player.getName().getString());
				MultiLoader.getInstance().sendPacketToPlayer(serverPlayer, new HelloPatPatPlayerS2CPacket());
			} else {
				PatPatServerPacketManager.PLAYER_VERSIONS.remove(player.getUUID());
				PatPat.LOGGER.debug("Player {} disconnected!", player.getName().getString());
			}
		});
	}

}
