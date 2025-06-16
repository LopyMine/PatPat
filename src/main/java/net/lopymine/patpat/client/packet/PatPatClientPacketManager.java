package net.lopymine.patpat.client.packet;

import lombok.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.*;
import net.lopymine.patpat.client.config.PatPatClientPlayerListConfig;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.client.render.PatPatClientRenderer.PacketPat;
import net.lopymine.patpat.common.Version;
import net.lopymine.patpat.compat.flashback.FlashbackManager;
import net.lopymine.patpat.compat.replaymod.ReplayModManager;
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
			handlePatting(packet, FlashbackManager.isInReplay() || ReplayModManager.isInReplay());
		});

		PatPatClientNetworkManager.registerReceiver(PatEntityS2CPacketV2.TYPE, packet -> {
			PatPatClientProxLibManager.disableIfEnabledBecauseReceivedPacketFromServer();
			handlePatting(packet, FlashbackManager.isInReplay() || ReplayModManager.isInReplay());
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
		PatPatClient.LOGGER.debug("[PING] Received HelloPatPatPlayerS2CPacket packet! PatPat Mod/Plugin installed on the server!");
		Version version = packet.getVersion();
		if (version.isInvalid()) {
			PatPatClient.LOGGER.warn("Received invalid server version in hello packet!");
			PatPatClientProxLibManager.setEnabledIfNotInReplay(false);
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V2_VERSION);
			// Since v2 packet version we started sending hello packets
			return;
		}
		// Example for the future packet versions:
		// if (version.isGreaterOrEqualThan(Version.PACKET_V3_VERSION)) {
		// 	 // stuff
		// } else
		if (version.isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			PatPatClientProxLibManager.setEnabledIfNotInReplay(false);
			PatPatClientPacketManager.setCurrentPatPatServerPacketVersion(Version.PACKET_V2_VERSION);
		}
		PatPatClient.LOGGER.debug("[PONG] Sending HelloPatPatServerC2S packet to the server...");
		PatPatClientNetworkManager.sendPacketToServer(new HelloPatPatServerC2SPacket());
	}

	public static void handlePatting(S2CPatPacket<?> packet, boolean replayModPacket) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClient.LOGGER.debug("Handle patting running");
		if (!config.getMainConfig().isModEnabled()) {
			PatPatClient.LOGGER.debug("Packet declined, because mod is disabled");
			return;
		}

		ClientLevel clientWorld = Minecraft.getInstance().level;
		LocalPlayer player = Minecraft.getInstance().player;
		if (clientWorld == null || player == null) {
			PatPatClient.LOGGER.debug("Packet declined, because world or player is null");
			return;
		}

		Entity pattedEntity = packet.getPattedEntity(clientWorld);
		if (!(pattedEntity instanceof LivingEntity pattedLivingEntity)) {
			PatPatClient.LOGGER.debug("Packet declined, because patted entity in not LivingEntity");
			return;
		}
		Entity playerEntity = packet.getWhoPattedEntity(clientWorld);
		if(playerEntity == null) {
			PatPatClient.LOGGER.debug("Packet declined, because who patted entity is null");
			return;
		}
		PatPatClient.LOGGER.debug("Pat packet from {} player", playerEntity.getName());
		if (!(playerEntity instanceof Player)) {
			PatPatClient.LOGGER.debug("Packet declined, because who patted entity in not PlayerEntity");
			return;
		}

		UUID pattedEntityUuid = pattedEntity.getUUID();
		UUID whoPattedUuid = playerEntity.getUUID();
		if (isBlocked(whoPattedUuid)) {
			PatPatClient.LOGGER.debug("Packet declined, because player uuid is blocked (/patpat-client list)");
			return;
		}
		if (pattedEntityUuid.equals(player.getUUID()) && !config.getMultiPlayerConfig().isPatMeEnabled()) {
			PatPatClient.LOGGER.debug("Packet declined, because option 'Pat Me' is disabled");
			return;
		}
		PatPatClientRenderer.serverPats.add(new PacketPat(pattedLivingEntity, PlayerConfig.of(playerEntity.getName().getString(), whoPattedUuid), player, replayModPacket));
	}

	public static boolean isBlocked(UUID playerUuid) {
		PlayerSocialManager socialManager = Minecraft.getInstance().getPlayerSocialManager();

		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientPlayerListConfig playerListConfig = PatPatClientPlayerListConfig.getInstance();

		return (config.getMultiPlayerConfig().getListMode() == ListMode.WHITELIST && !playerListConfig.getMap().containsKey(playerUuid))
				|| (config.getMultiPlayerConfig().getListMode() == ListMode.BLACKLIST && playerListConfig.getMap().containsKey(playerUuid))
				|| socialManager.isBlocked(playerUuid)
				|| socialManager.isHidden(playerUuid)
				/*? >=1.17 {*/ || socialManager.shouldHideMessageFrom(playerUuid)/*?}*/;
	}

	public static PatPacket<ServerLevel, ?> getPatPacket(Entity pattedEntity) {
		if (PatPatClientPacketManager.getCurrentPatPatServerPacketVersion().isGreaterOrEqualThan(Version.PACKET_V2_VERSION)) {
			PatPatClient.LOGGER.debug("Using v2 packets");
			return new PatEntityC2SPacketV2(pattedEntity);
		} else {
			PatPatClient.LOGGER.debug("Using v1 packets");
			return new PatEntityC2SPacket(pattedEntity);
		}
	}
}
