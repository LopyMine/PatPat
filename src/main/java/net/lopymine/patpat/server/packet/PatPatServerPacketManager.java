package net.lopymine.patpat.server.packet;

import net.lopymine.patpat.*;
import net.lopymine.patpat.entrypoint.ServerMultiLoader;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader.ServerPacketRegister;
import net.lopymine.patpat.entrypoint.loader.server.IServerModLoader.ServerPacketRegister.PacketRegistrationSide;
import net.lopymine.patpat.logger.PatLogger;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.*;

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

	public static final PatLogger LOGGER = PatPat.LOGGER.extend("PacketManager");

	public static final Map<UUID, Version> PLAYER_VERSIONS = new HashMap<>();

	private static final List<Predicate<ServerPlayer>> PACKET_TESTS = new ArrayList<>();

	public static void register(ServerPacketRegister register) {
		PACKET_TESTS.clear();
		PACKET_TESTS.add(PatPatServerListManager::canPat);
		PACKET_TESTS.add(PatPatServerRateLimitManager::canPat);

		// Hello packets
		register.register(HelloPatPatServerC2SPacket.TYPE, PacketRegistrationSide.C2S, PatPatServerPacketManager::handleHelloPacket);
		register.register(HelloPatPatPlayerS2CPacket.TYPE, PacketRegistrationSide.S2C, (player, packet) -> {});

		// Pat packets V2
		register.register(PatEntityC2SPacketV2.TYPE, PacketRegistrationSide.C2S, PatPatServerPacketManager::handlePacket);
		register.register(PatEntityS2CPacketV2.TYPE, PacketRegistrationSide.S2C, (player, packet) -> {});
		register.register(SelfPatEntityS2CPacketV2.TYPE, PacketRegistrationSide.S2C, (player, packet) -> {});

		// Pat packets V1
		register.register(PatEntityC2SPacket.TYPE, PacketRegistrationSide.C2S, PatPatServerPacketManager::handlePacket);
		register.register(PatEntityS2CPacket.TYPE, PacketRegistrationSide.S2C, (player, packet) -> {});
		register.register(SelfPatEntityS2CPacket.TYPE, PacketRegistrationSide.S2C, (player, packet) -> {});
	}

	private static void handleHelloPacket(ServerPlayer sender, HelloPatPatServerC2SPacket packet) {
		LOGGER.debug("Received hello packet from {}!", sender.getName().getString());
		Version version = packet.getVersion();
		if (version.isInvalid()) {
			LOGGER.error("Received invalid client version in hello packet from {}!", sender.getName().getString());
			PLAYER_VERSIONS.put(sender.getUUID(), Version.PACKET_V2_VERSION);
			// Since v2 packet version we started sending hello packets
			return;
		}
		LOGGER.debug("Player PatPat Version: {}", version);
		PLAYER_VERSIONS.put(sender.getUUID(), version);
	}

	@SuppressWarnings("ConstantConditions")
	public static void handlePacket(ServerPlayer sender, PatPacket<ServerLevel, ?> packet) {
		LOGGER.debug("Received pat packet from {}", sender.getName().getString());
		for (Predicate<ServerPlayer> packetTest : PACKET_TESTS) {
			if (!packetTest.test(sender)) {
				return;
			}
		}

		Level level = sender./*? >=1.20 {*/level()/*?} else {*//*level*//*?}*/;
		if (!(level instanceof ServerLevel serverWorld)) {
			return;
		}

		Entity entity = packet.getPattedEntity(serverWorld);
		if (!(entity instanceof LivingEntity)) {
			return;
		}

		if (entity.isInvisible()) {
			LOGGER.warn("Received packet from client, {} patted {}, but patted entity is invisible! This shouldn't happens because it should be checked at the client-side! Ignoring packet", sender.getName(), entity.getName());
			return;
		}

		ChunkPos chunkPos = /*? >=1.17 {*/entity.chunkPosition()/*?} else {*//*serverWorld.getChunk(entity./^? if >=1.17 {^/ getBlockPos() /^?} else {^//^blockPosition()^//^?}^/).getPos()*//*?}*/;
		for (ServerPlayer player : serverWorld.getChunkSource().chunkMap.getPlayers(chunkPos, false)) {
			if (player.equals(sender)) {
				continue;
			}
			LOGGER.debug("Sending pat packet to {} from {}", player.getName().getString(), sender.getName().getString());
			ServerMultiLoader.getInstance().sendPacketToPlayer(player, getPatPacket(entity, player));
		}
	}

	public static PatPacket<ClientLevel, ?> getPatPacket(Entity pattedEntity, Entity whoPattedEntity) {
		if (PLAYER_VERSIONS.get(whoPattedEntity.getUUID()).isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			LOGGER.debug("Getting pat packet... Using V2 version");
			return new PatEntityS2CPacketV2(pattedEntity, whoPattedEntity);
		} else {
			LOGGER.debug("Getting pat packet... Using V1 version");
			return new PatEntityS2CPacket(pattedEntity, whoPattedEntity);
		}
	}
}