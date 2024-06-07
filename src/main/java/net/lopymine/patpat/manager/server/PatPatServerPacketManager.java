package net.lopymine.patpat.manager.server;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.resourcepack.ListMode;
import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.packet.*;

public class PatPatServerPacketManager {

	private PatPatServerPacketManager(){
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(PatEntityC2SPacket.TYPE, (packet, sender, responseSender) -> {
			PatPatServerConfig config = PatPat.getConfig();
			GameProfile senderProfile = sender.getGameProfile();
			if ((config.getListMode() == ListMode.WHITELIST && !config.getPlayers().containsKey(senderProfile.getId())) || (config.getListMode() == ListMode.BLACKLIST && config.getPlayers().containsKey(sender.getUuid()))) {
				return;
			}
			ServerWorld serverWorld = sender.getServerWorld();
			Entity entity = serverWorld.getEntity(packet.getPattedEntityUuid());
			if (entity == null) {
				return;
			}
			ChunkPos chunkPos = sender.getChunkPos();
			for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
				if (player == sender) {
					continue;
				}
				ServerPlayNetworking.send(player, new PatEntityS2CPacket(packet.getPattedEntityUuid(), senderProfile.getId(), senderProfile.getName(), packet.isDonor()));
			}
		});
	}
}
