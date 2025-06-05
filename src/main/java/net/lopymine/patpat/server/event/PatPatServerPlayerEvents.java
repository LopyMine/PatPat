package net.lopymine.patpat.server.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.packet.s2c.HelloPatPatPlayerS2CPacket;
import net.lopymine.patpat.server.packet.PatPatServerNetworkManager;
import net.lopymine.patpat.server.packet.PatPatServerPacketManager;
import net.minecraft.server.level.ServerPlayer;

public class PatPatServerPlayerEvents {

	private PatPatServerPlayerEvents() {
		throw new IllegalStateException("Events class");
	}

	public static void register() {
		ServerPlayConnectionEvents.JOIN.register((handler, packetSender, server) -> { ServerPlayer player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;
			PatPatServerPacketManager.PLAYER_VERSIONS.put(player.getUUID(), Version.PACKET_V1_VERSION);
			PatPat.LOGGER.debug("Player {} joined, send hello packet", player.getName().getString());
			PatPatServerNetworkManager.sendPacketToPlayer(player, new HelloPatPatPlayerS2CPacket());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> { ServerPlayer player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;
			PatPatServerPacketManager.PLAYER_VERSIONS.remove(player.getUUID());
			PatPat.LOGGER.debug("Player {} quit", player.getName().getString());
		});
	}

}
