package net.lopymine.patpat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lopymine.patpat.packet.PatEntityC2SPacket;
import net.lopymine.patpat.packet.PatEntityS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PatPat implements ModInitializer {

	public static final String MOD_ID = "patpat";
	public static final Logger LOGGER = LoggerFactory.getLogger("Patpat");

	// TODO
	//  Сделать возможность блокировки пользователя на время при ситуации, если он отправляет
	//  слишком много пакетов в секунду/5 секунд (время бана и количество пакетов настраивается в кфг)
	private static final Set<UUID> BANNED_PLAYERS = new HashSet<>();


	@Override
	public void onInitialize() {
		LOGGER.info("PatPat Initialized");
		ServerPlayNetworking.registerGlobalReceiver(PatEntityC2SPacket.TYPE, (packet, sender, responseSender) -> {
			if (BANNED_PLAYERS.contains(sender.getUuid())) {
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