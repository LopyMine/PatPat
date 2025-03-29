package net.lopymine.patpat.manager.server.command;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.network.ServerPlayerEntity;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.config.server.*;
import net.lopymine.patpat.extension.PlayerExtension;

import java.util.*;

@ExtensionMethod(PlayerExtension.class)
public class RateLimitManager {

	private RateLimitManager() {
		throw new IllegalStateException("Manager class");
	}

	private static Timer timer;

	private static final Map<UUID, Integer> uuidToPat = new HashMap<>();

	public static int getAvailablePats(UUID uuid) {
		return uuidToPat.getOrDefault(uuid, PatPatServerConfig.getInstance().getRateLimitConfig().getTokenLimit());
	}

	public static boolean canPat(ServerPlayerEntity player) {
		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		if (player.hasPermission(config.getPermissionBypass())) {
			return true;
		}
		UUID uuid = player.getUuid();
		if (!config.isEnabled()) {
			return true;
		}
		int availablePats = uuidToPat.getOrDefault(
				uuid,
				config.getTokenLimit()
		) - 1;
		if (availablePats < 0) {
			return false;
		}
		uuidToPat.put(uuid, availablePats);
		return true;
	}

	public static void addPats(int token) {
		int tokenLimit = PatPatServerConfig.getInstance().getRateLimitConfig().getTokenLimit();
		for (Iterator<Map.Entry<UUID, Integer>> it = uuidToPat.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<UUID, Integer> entry = it.next();
			int value = entry.getValue() + token;
			if (value > tokenLimit) {
				it.remove();
				continue;
			}
			uuidToPat.put(entry.getKey(), value);
		}
	}

	public static void runTask() {
		if (timer != null) {
			return;
		}
		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		if (!config.isEnabled()) {
			return;
		}
		Time configInterval = config.getTokenIncrementInterval();
		long period = configInterval.getValue() * configInterval.getUnit().getMultiplier() * 1000L;
		timer = new Timer(PatPat.MOD_ID + "/RateLimitTask");
		timer.scheduleAtFixedRate(new RateLimitTask(), 0, period);
	}

	public static void stopTask() {
		if (timer != null) {
			timer.cancel();
		}
		timer = null;
	}

	public static void reloadTask() {
		stopTask();
		runTask();
	}
}
