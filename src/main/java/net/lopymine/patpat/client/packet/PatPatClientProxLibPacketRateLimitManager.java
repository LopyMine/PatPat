package net.lopymine.patpat.client.packet;

import net.minecraft.client.multiplayer.ClientLevel;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.lopymine.patpat.client.config.PatPatClientConfig;

public class PatPatClientProxLibPacketRateLimitManager {

	private PatPatClientProxLibPacketRateLimitManager() {
		throw new IllegalStateException("Manager class");
	}

	private static int ticks;
	private static int packetsSentPerSecond;

	public static void register() {
		ClientTickEvents.END_WORLD_TICK.register(PatPatClientProxLibPacketRateLimitManager::tick);
	}

	public static void tick(ClientLevel level) {
		if (PatPatClientProxLibPacketRateLimitManager.ticks == Integer.MAX_VALUE) {
			PatPatClientProxLibPacketRateLimitManager.ticks = 0;
		}
		if (PatPatClientProxLibPacketRateLimitManager.ticks++ % 20 == 0) {
			tickPerSecond();
		}
	}

	private static void tickPerSecond() {
		PatPatClientProxLibPacketRateLimitManager.packetsSentPerSecond = 0;
	}

	public static void countPacket() {
		PatPatClientProxLibPacketRateLimitManager.packetsSentPerSecond++;
	}

	public static boolean isLimitExceeded() {
		return PatPatClientProxLibPacketRateLimitManager.packetsSentPerSecond >= PatPatClientConfig.getInstance().getProximityPacketsConfig().getMaxPacketsPerSecond();
	}
}
