package net.lopymine.patpat.server.packet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.common.Version;
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

	public static final Map<UUID, Version> PLAYER_VERSIONS = new HashMap<>();

	private static final List<Predicate<ServerPlayer>> PACKET_TESTS = new ArrayList<>();

	public static void register() {
		PACKET_TESTS.clear();
		PACKET_TESTS.add(PatPatServerListManager::canPat);
		PACKET_TESTS.add(PatPatServerRateLimitManager::canPat);

		PatPatServerPacketManager.registerPackets();
		PatPatServerNetworkManager.registerReceiver(PatEntityC2SPacket.TYPE, PatPatServerPacketManager::handlePacket);
		PatPatServerNetworkManager.registerReceiver(PatEntityC2SPacketV2.TYPE, PatPatServerPacketManager::handlePacket);
		PatPatServerNetworkManager.registerReceiver(HelloPatPatServerC2SPacket.TYPE, PatPatServerPacketManager::handleHelloPacket);
	}

	private static void handleHelloPacket(ServerPlayer sender, HelloPatPatServerC2SPacket packet) {
		PatPat.LOGGER.debug("Received hello packet from {}!", sender.getName().getString());
		Version version = packet.getVersion();
		if (version.isInvalid()) {
			PatPat.LOGGER.warn("Received invalid client version in hello packet from {}!", sender.getName().getString());
			PLAYER_VERSIONS.put(sender.getUUID(), Version.PACKET_V2_VERSION);
			// Since v2 packet version we started sending hello packets
			return;
		}
		PatPat.LOGGER.debug("Player PatPat version: {}", version);
		PLAYER_VERSIONS.put(sender.getUUID(), version);
	}

	public static void handlePacket(ServerPlayer sender, PatPacket<ServerLevel, ?> packet) {
		PatPat.LOGGER.warn("Received pat packet from {}!", sender.getName().getString());
		for (Predicate<ServerPlayer> packetTest : PACKET_TESTS) {
			if (!packetTest.test(sender)) {
				return;
			}
		}

		ServerLevel serverWorld = (ServerLevel) sender./*? >=1.18 {*/level()/*?} else {*//*world*//*?}*/;
		Entity entity = packet.getPattedEntity(serverWorld);
		if (!(entity instanceof LivingEntity)) {
			return;
		}

		if (entity.isInvisible()) {
			PatPat.LOGGER.warn("Received packet from client, {} patted {}, but patted entity is invisible! This shouldn't happens because it should checks at client-side!", sender.getName(), entity.getName());
			return;
		}

		ChunkPos chunkPos = /*? >=1.17 {*/entity.chunkPosition()/*?} else {*//*serverWorld.getChunk(entity.getBlockPos()).getPos()*//*?}*/;
		for (ServerPlayer player : PlayerLookup.tracking(serverWorld, chunkPos)) {
			if (player.equals(sender)) {
				continue;
			}
			PatPat.LOGGER.debug("Sending pat packet to {}", player.getName().getString());
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

	public static PatPacket<ClientLevel, ?> getPatPacket(Entity pattedEntity, Entity whoPattedEntity) {
		if (PLAYER_VERSIONS.get(whoPattedEntity.getUUID()).isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			PatPat.LOGGER.warn("Using v2 packets");
			return new PatEntityS2CPacketV2(pattedEntity, whoPattedEntity);
		} else {
			PatPat.LOGGER.warn("Using v1 packets");
			return new PatEntityS2CPacket(pattedEntity, whoPattedEntity);
		}
	}
}