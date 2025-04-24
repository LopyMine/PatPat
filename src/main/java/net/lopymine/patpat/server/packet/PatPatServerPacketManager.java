package net.lopymine.patpat.server.packet;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.packet.PatPatClientPacketManager;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.packet.c2s.*;
import net.lopymine.patpat.packet.s2c.*;
import net.lopymine.patpat.server.ratelimit.*;

import java.util.*;
import java.util.function.Predicate;

public class PatPatServerPacketManager {

	private PatPatServerPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	private static final HashMap<UUID, Boolean> PLAYER_PROTOCOLS = new HashMap<>();

	private static final List<Predicate<ServerPlayerEntity>> HANDLERS = new ArrayList<>();

	public static void register() {
		HANDLERS.clear();
		HANDLERS.add(PatPatServerListManager::canPat);
		HANDLERS.add(PatPatServerRateLimitManager::canPat);

		ServerPlayConnectionEvents.JOIN.register((handler, packetSender, server) -> {
			PLAYER_PROTOCOLS.put(handler.getPlayer().getUuid(), false);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			PLAYER_PROTOCOLS.remove(handler.getPlayer().getUuid());
		});

		registerPackets();

		PatPatServerNetworkManager.registerReceiver(PatEntityC2SPacket.TYPE, PatPatServerPacketManager::handlePacket);
		PatPatServerNetworkManager.registerReceiver(PatEntityC2SPacketV2.TYPE, PatPatServerPacketManager::handlePacket);

		PatPatServerNetworkManager.registerReceiver(HelloPatPatServerC2SPacket.TYPE, (sender, packet) -> {
			PLAYER_PROTOCOLS.put(sender.getUuid(), true);
			PatPatServerNetworkManager.sendPacketToPlayer(sender, new HelloPatPatPlayerS2CPacket());
		});
	}

	public static void handlePacket(ServerPlayerEntity sender, PatPacket<ServerWorld> packet) {
		for (Predicate<ServerPlayerEntity> handler : HANDLERS) {
			if (!handler.test(sender)) {
				return;
			}
		}

		ServerWorld serverWorld = (ServerWorld) sender./*? >=1.18 {*/getWorld()/*?} else {*//*world*//*?}*/;
		Entity entity = packet.getPattedEntity(serverWorld);
		if (!(entity instanceof LivingEntity)) {
			return;
		}

		if (entity.isInvisible()) {
			PatPat.LOGGER.warn("Received packet from client, {} patted {}, but patted entity is invisible! This shouldn't happens because it should checks at client-side!", sender.getName(), entity.getName());
			return;
		}

		ChunkPos chunkPos = /*? >=1.17 {*/entity.getChunkPos()/*?} else {*//*serverWorld.getChunk(entity.getBlockPos()).getPos()*//*?}*/;
		for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
			if (player.equals(sender)) {
				continue;
			}

			PatPatServerNetworkManager.sendPacketToPlayer(player, getPatPacket(entity, player));
		}
	}

	private static void registerPackets() {
		// Register all packets
		//? >=1.20.5 {
		PayloadTypeRegistry.playC2S().register(HelloPatPatServerC2SPacket.TYPE, HelloPatPatServerC2SPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(HelloPatPatPlayerS2CPacket.TYPE, HelloPatPatPlayerS2CPacket.CODEC);

		// v2
		PayloadTypeRegistry.playC2S().register(PatEntityC2SPacketV2.TYPE, PatEntityC2SPacketV2.CODEC);
		PayloadTypeRegistry.playS2C().register(PatEntityS2CPacketV2.TYPE, PatEntityS2CPacketV2.CODEC);

		// v1
		PayloadTypeRegistry.playC2S().register(PatEntityC2SPacket.TYPE, PatEntityC2SPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(PatEntityS2CPacket.TYPE, PatEntityS2CPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(PatEntityForReplayModS2CPacket.TYPE, PatEntityForReplayModS2CPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(PatEntityForReplayModS2CPacketV2.TYPE, PatEntityForReplayModS2CPacketV2.CODEC);
		//?}
	}

	public static PatPacket<ClientWorld> getPatPacket(Entity pattedEntity, Entity whoPattedEntity) {
		if (PLAYER_PROTOCOLS.get(whoPattedEntity.getUuid())) {
			return new PatEntityS2CPacketV2(pattedEntity, whoPattedEntity);
		} else {
			return new PatEntityS2CPacket(pattedEntity, whoPattedEntity);
		}
	}
}