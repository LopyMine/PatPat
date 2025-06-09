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
		if (ticks == Integer.MAX_VALUE) {
			ticks = 0;
		}
		if (ticks++ % 20 == 0) {
			tickPerSecond();
		}
	}

	private static void tickPerSecond() {
		packetsSentPerSecond = 0;
	}

	public static void countPacket() {
		packetsSentPerSecond++;
	}

	public static boolean isLimitExceeded() {
		return packetsSentPerSecond >= PatPatClientConfig.getInstance().getProximityPacketsConfig().getMaxPacketsPerSecond();
	}
}
