package net.lopymine.patpat.server.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.extension.PlayerExtension;
import net.lopymine.patpat.server.config.*;
import net.lopymine.patpat.server.config.sub.PatPatServerRateLimitConfig;
import net.minecraft.server.level.ServerPlayer;
import net.lopymine.patpat.common.config.time.Time;

import java.util.*;

@ExtensionMethod(PlayerExtension.class)
public class PatPatServerRateLimitManager {

	private PatPatServerRateLimitManager() {
		throw new IllegalStateException("Manager class");
	}

	private static Timer timer;

	private static final Map<UUID, Integer> uuidToPat = new HashMap<>();

	public static int getAvailablePats(UUID uuid) {
		return uuidToPat.getOrDefault(uuid, PatPatServerConfig.getInstance().getRateLimitConfig().getTokenLimit());
	}

	public static boolean canPat(ServerPlayer player) {
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		if (!config.isEnabled()) {
			return true;
		}
		if (player.hasPermission(config.getPermissionBypass())) {
			return true;
		}
		UUID uuid = player.getUUID();
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
		PatPatServerRateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		if (!config.isEnabled()) {
			return;
		}
		Time configInterval = config.getTokenIncrementInterval();
		long period = configInterval.getValue() * configInterval.getUnit().getMultiplier() * 1000L;
		timer = new Timer(PatPat.MOD_ID + "/PatPatServerRateLimitTask");
		timer.scheduleAtFixedRate(new PatPatServerRateLimitTask(), 0, period);
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
