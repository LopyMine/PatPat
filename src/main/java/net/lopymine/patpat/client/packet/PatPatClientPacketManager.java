package net.lopymine.patpat.client.packet;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.fabric.api.client.networking.v1.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.packet.PatPacket;
import net.lopymine.patpat.packet.c2s.*;
import net.lopymine.patpat.packet.s2c.*;

import java.util.UUID;

public class PatPatClientPacketManager {

	private PatPatClientPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	@Getter
	@Setter
	private static boolean useV2PatPackets = false;

	public static void register() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			PatPatClient.LOGGER.debug("[PING] Sending HelloPatPatServerC2S packet to the server...");
			ClientPlayNetworking.send(new HelloPatPatServerC2SPacket());
		});

		PatPatClientNetworkManager.registerReceiver(HelloPatPatPlayerS2CPacket.TYPE, (packet) -> {
			PatPatClient.LOGGER.debug("[PONG] Received HelloPatPatPlayerS2CPacket packet! PatPat Mod/Plugin installed on the server!");
			PatPatClientProxLibManager.setEnabled(false);
			PatPatClientPacketManager.setUseV2PatPackets(true);
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			PatPatClientPacketManager.setUseV2PatPackets(false);
			PatPatClient.LOGGER.debug("Disconeccting, disabling v2 packets!!");
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityS2CPacket.TYPE, (packet) -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, false);
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityS2CPacketV2.TYPE, (packet) -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, false);
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityForReplayModS2CPacket.TYPE, (packet) -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, true);
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityForReplayModS2CPacketV2.TYPE, (packet) -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, true);
		});
	}

	public static void handlePatting(S2CPatPacket packet, boolean replayModPacket) {
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
		if (isBlocked(config, whoPattedUuid)) {
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

	public static boolean isBlocked(PatPatClientConfig config, UUID playerUuid) {
		SocialInteractionsManager socialManager = MinecraftClient.getInstance().getSocialInteractionsManager();
		return (config.getServerConfig().getListMode() == ListMode.WHITELIST && !config.getServerConfig().getPlayers().containsKey(playerUuid))
				|| (config.getServerConfig().getListMode() == ListMode.BLACKLIST && config.getServerConfig().getPlayers().containsKey(playerUuid))
				|| socialManager.isPlayerBlocked(playerUuid)
				|| socialManager.isPlayerHidden(playerUuid)
				/*? >=1.17 {*/ || socialManager.isPlayerMuted(playerUuid)/*?}*/;
	}

	public static PatPacket<ServerWorld> getPatPacket(Entity pattedEntity) {
		if (PatPatClientPacketManager.isUseV2PatPackets()) {
			PatPatClient.LOGGER.debug("Using v2 packets");
			return new PatEntityC2SPacketV2(pattedEntity);
		} else {
			PatPatClient.LOGGER.debug("Using v1 packets");
			return new PatEntityC2SPacket(pattedEntity);
		}
	}
}
