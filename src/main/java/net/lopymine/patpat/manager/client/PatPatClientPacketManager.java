package net.lopymine.patpat.manager.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.*;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.packet.*;
import net.lopymine.patpat.utils.WorldUtils;

import java.util.UUID;

public class PatPatClientPacketManager {

	private PatPatClientPacketManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(
				PatEntityForReplayModS2CPacket./*? >=1.19.4 {*/TYPE/*?} else {*//*PACKET_ID*//*?}*/,
				/*? >=1.20.5 {*/(packet, context) -> {/*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, player, responseSender) -> {*//*?} else {*//*(client, handler, buf, responseSender) -> { PatEntityS2CPacket packet = new PatEntityS2CPacket(buf);*//*?}*/
					PatPatClientPacketManager.handlePatting(packet.getWhoPattedUuid(), packet.getPattedEntityUuid(), true);
				}
		);

		ClientPlayNetworking.registerGlobalReceiver(
				PatEntityS2CPacket./*? >=1.19.4 {*/TYPE/*?} else {*//*PACKET_ID*//*?}*/,
				/*? >=1.20.5 {*/(packet, context) -> {/*?} elif <=1.20.4 && >=1.19.4 {*//*(packet, player, responseSender) -> {*//*?} else {*//*(client, handler, buf, responseSender) -> { PatEntityS2CPacket packet = new PatEntityS2CPacket(buf);*//*?}*/
					PatPatClientPacketManager.handlePatting(packet.getWhoPattedUuid(), packet.getPattedEntityUuid(), false);
				}
		);
	}

	private static void handlePatting(UUID whoPattedUuid, UUID pattedEntityUuid, boolean replayModPacket) {
		if (PatPatClient.getConfig().isDebugLogEnabled()) {
			PatPatClient.info("Received packet from server, {} patted {}, replayModPacket: {}", whoPattedUuid, pattedEntityUuid, replayModPacket);
		}

		ClientWorld clientWorld = MinecraftClient.getInstance().world;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		PatPatClientConfig config = PatPatClient.getConfig();
		if (!config.isModEnabled()) {
			return;
		}
		if (clientWorld == null) {
			return;
		}
		boolean pattedMe = pattedEntityUuid.equals(MinecraftClient.getInstance().getSession()/*? >=1.20 {*/.getUuidOrNull()/*?} else {*//*.getProfile().getId()*//*?}*/);
		if (pattedMe && !config.isPatMeEnabled()) {
			return;
		}
		if (isBlocked(config, whoPattedUuid)) {
			return;
		}
		Entity pattedEntity = WorldUtils.getEntity(clientWorld, pattedEntityUuid);
		if (!(pattedEntity instanceof LivingEntity livingEntity)) {
			return;
		}
		Entity playerEntity = WorldUtils.getEntity(clientWorld, whoPattedUuid);
		if (!(playerEntity instanceof PlayerEntity)) {
			return;
		}
		PatEntity patEntity = PatPatClientManager.pat(livingEntity, PlayerConfig.of(playerEntity.getName().getString(), whoPattedUuid));
		if (config.isSoundsEnabled() && !replayModPacket) {
			PatPatClientSoundManager.playSound(patEntity, player, config.getSoundsVolume());
		}
	}

	private static boolean isBlocked(PatPatClientConfig config, UUID playerUuid) {
		SocialInteractionsManager socialManager = MinecraftClient.getInstance().getSocialInteractionsManager();
		return (config.getListMode() == ListMode.WHITELIST && !config.getPlayers().containsKey(playerUuid))
				|| (config.getListMode() == ListMode.BLACKLIST && config.getPlayers().containsKey(playerUuid))
				|| socialManager.isPlayerBlocked(playerUuid)
				|| socialManager.isPlayerHidden(playerUuid)
				/*? >=1.17 {*/ || socialManager.isPlayerMuted(playerUuid)/*?}*/;
	}
}
