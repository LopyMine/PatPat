package net.lopymine.patpat.manager.server;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.packet.*;

public class PatPatServerPacketManager {
	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(PatEntityC2SPacket.TYPE, (packet, sender, responseSender) -> {
			if (PatPat.getConfig().getEnabledOne().getPlayers().containsKey(sender.getUuid())) {
				return;
			}
			ServerWorld serverWorld = sender.getServerWorld();
			Entity entity = serverWorld.getEntity(packet.getPatEntityUuid());
			if (entity == null) {
				return;
			}
			ChunkPos chunkPos = sender.getChunkPos();
			for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
				if (player == sender) {
					continue;
				}
				ServerPlayNetworking.send(player, new PatEntityS2CPacket(packet.getPattingPlayerUuid(), packet.getPatEntityUuid()));
			}
		});
	}
}
