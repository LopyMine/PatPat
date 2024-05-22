package net.lopymine.patpat.manager.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.packet.PatEntityS2CPacket;
import net.lopymine.patpat.utils.WorldUtils;

import java.util.UUID;

public class PatPatClientPacketManager {
	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(PatEntityS2CPacket.TYPE, ((packet, player, responseSender) -> {
			ClientWorld clientWorld = player.clientWorld;
			if (clientWorld == null) {
				return;
			}
			UUID pattingPlayerUuid = packet.getPattingPlayerUuid();
			if (PatPatClient.getConfig().containsInEnabledList(pattingPlayerUuid)
					|| MinecraftClient.getInstance().getSocialInteractionsManager().isPlayerBlocked(pattingPlayerUuid)
					|| MinecraftClient.getInstance().getSocialInteractionsManager().isPlayerHidden(pattingPlayerUuid)) {
//				    || MinecraftClient.getInstance().getSocialInteractionsManager().isPlayerMuted(packet.getPatEntityUuid()) // TODO нужно ли делать проверку на мут человека?
				return;
			}
			Entity entity = WorldUtils.getEntity(clientWorld, packet.getPatEntityUuid());
			if (!(entity instanceof LivingEntity livingEntity)) {
				return;
			}
			PatEntity patEntity = PatPatClientManager.pat(livingEntity, pattingPlayerUuid);
			PatPatClientSoundManager.playSound(patEntity, player);
		}));
	}
}
