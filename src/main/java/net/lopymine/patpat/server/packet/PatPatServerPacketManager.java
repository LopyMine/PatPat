package net.lopymine.patpat.server.packet;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.config.Version;
import net.lopymine.patpat.packet.PatPatPacketType;
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

	private static final HashMap<UUID, Version> PLAYER_VERSIONS = new HashMap<>();

	private static final List<Predicate<ServerPlayerEntity>> HANDLERS = new ArrayList<>();

	public static void register() {
		HANDLERS.clear();
		HANDLERS.add(PatPatServerListManager::canPat);
		HANDLERS.add(PatPatServerRateLimitManager::canPat);

		ServerPlayConnectionEvents.JOIN.register((handler, packetSender, server) -> {
			ServerPlayerEntity player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;

			PLAYER_VERSIONS.put(player.getUuid(), Version.PACKET_V1_VERSION);
			PatPat.LOGGER.warn("Player {} just joined!", player.getName().getString());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = /*? if >=1.21 {*/ handler.getPlayer() /*?} else {*/ /*handler.player *//*?}*/;

			PLAYER_VERSIONS.remove(player.getUuid());
			PatPat.LOGGER.warn("Player {} disconnected!", player.getName().getString());
		});

		registerPackets();

		PatPatServerNetworkManager.registerReceiver(PatEntityC2SPacket.TYPE, PatPatServerPacketManager::handlePacket);
		PatPatServerNetworkManager.registerReceiver(PatEntityC2SPacketV2.TYPE, PatPatServerPacketManager::handlePacket);

		PatPatServerNetworkManager.registerReceiver(HelloPatPatServerC2SPacket.TYPE, (sender, packet) -> {
			PatPatServerNetworkManager.sendPacketToPlayer(sender, new HelloPatPatPlayerS2CPacket());
			PatPat.LOGGER.warn("Received hello packet from {}!", sender.getName().getString());
			Version version = packet.getVersion();
			if (version.isInvalid()) {
				PatPat.LOGGER.warn("Received invalid client version in hello packet from {}!", sender.getName().getString());
				PLAYER_VERSIONS.put(sender.getUuid(), Version.PACKET_V2_VERSION);
				// We started sending this packet since v2 version,
				// so receiving this packet means that the client has at least 1.2.0 mod version (V2 Packet version)
				return;
			}
			PLAYER_VERSIONS.put(sender.getUuid(), version);
		});
	}

	public static void handlePacket(ServerPlayerEntity sender, PatPacket<ServerWorld, ?> packet) {
		PatPat.LOGGER.warn("Received pat packet from {}!", sender.getName().getString());
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
		registerC2SPacket(HelloPatPatServerC2SPacket.TYPE);
		registerS2CPacket(HelloPatPatPlayerS2CPacket.TYPE);

		// v2
		registerC2SPacket(PatEntityC2SPacketV2.TYPE);
		registerS2CPacket(PatEntityS2CPacketV2.TYPE);
		registerS2CPacket(PatEntityForReplayModS2CPacketV2.TYPE);

		// v1
		registerC2SPacket(PatEntityC2SPacket.TYPE);
		registerS2CPacket(PatEntityS2CPacket.TYPE);
		registerS2CPacket(PatEntityForReplayModS2CPacket.TYPE);
		//?}
	}

	//? >=1.20.5 {
	private static <T extends BasePatPatPacket<T>> void  registerC2SPacket(PatPatPacketType<T> type) {
		PayloadTypeRegistry.playC2S().register(type.getPacketId(), type.getCodec());
	}
	private static <T extends BasePatPatPacket<T>> void  registerS2CPacket(PatPatPacketType<T> type) {
		PayloadTypeRegistry.playS2C().register(type.getPacketId(), type.getCodec());
	}
	//?}

	public static PatPacket<ClientWorld, ?> getPatPacket(Entity pattedEntity, Entity whoPattedEntity) {
		if (PLAYER_VERSIONS.get(whoPattedEntity.getUuid()).isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			PatPat.LOGGER.warn("Using v2 packets");
			return new PatEntityS2CPacketV2(pattedEntity, whoPattedEntity);
		} else {
			PatPat.LOGGER.warn("Using v1 packets");
			return new PatEntityS2CPacket(pattedEntity, whoPattedEntity);
		}
	}
}