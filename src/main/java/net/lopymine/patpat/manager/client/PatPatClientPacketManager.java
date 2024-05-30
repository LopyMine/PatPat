package net.lopymine.patpat.manager.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.packet.PatEntityS2CPacket;
import net.lopymine.patpat.utils.WorldUtils;

import java.util.UUID;

public class PatPatClientPacketManager {
	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(PatEntityS2CPacket.TYPE, ((packet, player, responseSender) -> {
			PatPatClientConfig config = PatPatClient.getConfig();
			if (!config.isModEnabled()) {
				return;
			}
			ClientWorld clientWorld = player.clientWorld;
			if (clientWorld == null) {
				return;
			}
			String playerName = packet.getPlayerName();
			UUID playerUuid = packet.getPlayerUuid();
			UUID pattedEntityUuid = packet.getPattedEntityUuid();
			boolean pattedMe = MinecraftClient.getInstance().uuidEquals(pattedEntityUuid);
			if (pattedMe && !config.isPatMeEnabled()) {
				return;
			}
			if ((config.getListMode() == ListMode.WHITELIST && !config.getPlayers().containsKey(playerUuid))
					|| (config.getListMode() == ListMode.BLACKLIST && config.getPlayers().containsKey(playerUuid))
					|| MinecraftClient.getInstance().getSocialInteractionsManager().isPlayerBlocked(playerUuid)
					|| MinecraftClient.getInstance().getSocialInteractionsManager().isPlayerHidden(playerUuid)
					|| MinecraftClient.getInstance().getSocialInteractionsManager().isPlayerMuted(playerUuid)) {
				return;
			}
			Entity entity = WorldUtils.getEntity(clientWorld, pattedEntityUuid);
			if (!(entity instanceof LivingEntity livingEntity)) {
				return;
			}
			PatEntity patEntity = PatPatClientManager.pat(livingEntity, PlayerConfig.of(playerName, playerUuid));
			if (config.isSoundsEnabled()) {
				PatPatClientSoundManager.playSound(patEntity, player, config.getSoundsVolume());
			}
		}));
	}
}