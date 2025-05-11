package net.lopymine.patpat.server.event;

import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.server.packet.PatPatServerPacketManager;

public class PatPatServerPlayerEvents {

	public static void register() {
		ServerPlayConnectionEvents.JOIN.register((handler, packetSender, server) -> { ServerPlayerEntity player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;
			PatPatServerPacketManager.PLAYER_VERSIONS.put(player.getUuid(), Version.PACKET_V1_VERSION);
			PatPat.LOGGER.warn("Player {} just joined!", player.getName().getString());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> { ServerPlayerEntity player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;
			PatPatServerPacketManager.PLAYER_VERSIONS.remove(player.getUuid());
			PatPat.LOGGER.warn("Player {} disconnected!", player.getName().getString());
		});
	}

}
