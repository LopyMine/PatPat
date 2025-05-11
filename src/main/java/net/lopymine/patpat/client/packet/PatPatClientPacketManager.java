package net.lopymine.patpat.client.packet;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.config.sub.PatPatClientPlayerListConfig;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.packet.c2s.*;
import net.lopymine.patpat.packet.s2c.*;

import java.util.UUID;

public class PatPatClientPacketManager {

	private PatPatClientPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	@Getter
	@Setter
	private static Version currentPatPatServerPacketVersion = Version.PACKET_V1_VERSION;

	public static void register() {
		PatPatClientNetworkManager.registerReceiver(HelloPatPatPlayerS2CPacket.TYPE, PatPatClientPacketManager::handleHelloPacket);

		PatPatClientNetworkManager.registerReceiver(PatEntityS2CPacket.TYPE, packet -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, false);
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityS2CPacketV2.TYPE, packet -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, false);
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityForReplayModS2CPacket.TYPE, packet -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, true);
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityForReplayModS2CPacketV2.TYPE, packet -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, true);
		});
	}

	private static void handleHelloPacket(HelloPatPatPlayerS2CPacket packet) {
		PatPatClient.LOGGER.debug("[PONG] Received HelloPatPatPlayerS2CPacket packet! PatPat Mod/Plugin installed on the server!");
		Version version = packet.getVersion();
		if (version.isInvalid()) {
			PatPatClient.LOGGER.warn("Received invalid server version in hello packet!");
			PatPatClientProxLibManager.setEnabled(false);
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V2_VERSION);
			// Since v2 packet version we started sending hello packets
			return;
		}
		// Example for the future packet versions:
		// if (version.isGreaterOrEqualThan(Version.PACKET_V3_VERSION)) {
		// 	 // stuff
		// } else
		if (version.isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			PatPatClientProxLibManager.setEnabled(false);
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V2_VERSION);
		}
	}

	public static void handlePatting(S2CPatPacket<?> packet, boolean replayModPacket) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		System.out.println("0");
		if (!config.getMainConfig().isModEnabled()) {
			System.out.println("1");
			return;
		}

		ClientWorld clientWorld = MinecraftClient.getInstance().world;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (clientWorld == null || player == null) {
			System.out.println("2");
			return;
		}

		Entity pattedEntity = packet.getPattedEntity(clientWorld);
		if (!(pattedEntity instanceof LivingEntity livingEntity)) {
			System.out.println("3");
			return;
		}
		Entity playerEntity = packet.getWhoPattedEntity(clientWorld);
		System.out.println(playerEntity.getName().getString());
		if (!(playerEntity instanceof PlayerEntity)) {
			System.out.println("4");
			return;
		}

		UUID pattedEntityUuid = pattedEntity.getUuid();
		UUID whoPattedUuid = playerEntity.getUuid();
		if (isBlocked(whoPattedUuid)) {
			System.out.println("5");
			return;
		}
		if (pattedEntityUuid.equals(player.getUuid()) && !config.getServerConfig().isPatMeEnabled()) {
			System.out.println("6");
			return;
		}
		System.out.println("7");
		PatEntity patEntity = PatPatClientManager.pat(livingEntity, PlayerConfig.of(playerEntity.getName().getString(), whoPattedUuid));
		if (config.getSoundsConfig().isSoundsEnabled() && !replayModPacket) {
			PatPatClientSoundManager.playSound(patEntity, player, config.getSoundsConfig().getSoundsVolume());
		}
	}

	public static boolean isBlocked(UUID playerUuid) {
		SocialInteractionsManager socialManager = MinecraftClient.getInstance().getSocialInteractionsManager();

		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientPlayerListConfig playerListConfig = PatPatClientPlayerListConfig.getInstance();

		return (config.getServerConfig().getListMode() == ListMode.WHITELIST && !playerListConfig.getMap().containsKey(playerUuid))
				|| (config.getServerConfig().getListMode() == ListMode.BLACKLIST && playerListConfig.getMap().containsKey(playerUuid))
				|| socialManager.isPlayerBlocked(playerUuid)
				|| socialManager.isPlayerHidden(playerUuid)
				/*? >=1.17 {*/ || socialManager.isPlayerMuted(playerUuid)/*?}*/;
	}

	public static PatPacket<ServerWorld, ?> getPatPacket(Entity pattedEntity) {
		if (PatPatClientPacketManager.getCurrentPatPatServerPacketVersion().isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			PatPatClient.LOGGER.debug("Using v2 packets");
			return new PatEntityC2SPacketV2(pattedEntity);
		} else {
			PatPatClient.LOGGER.debug("Using v1 packets");
			return new PatEntityC2SPacket(pattedEntity);
		}
	}
}
