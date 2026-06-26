package net.lopymine.patpat.client.packet;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.entrypoint.ClientMultiLoader;
import net.minecraft.client.multiplayer.ClientLevel;

public class PatPatClientProxLibPacketRateLimitManager {

	private static int ticks;
	private static int packetsSentPerSecond;
	private PatPatClientProxLibPacketRateLimitManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		ClientMultiLoader.getInstance().registerAfterWorldTickListener(PatPatClientProxLibPacketRateLimitManager::tick);
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
