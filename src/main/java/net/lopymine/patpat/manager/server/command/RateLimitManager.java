package net.lopymine.patpat.manager.server.command;

import net.minecraft.server.ServerTask;

import net.lopymine.patpat.config.server.*;

import java.util.*;

public class RateLimitManager {

	private RateLimitManager() {
		throw new IllegalStateException("Manager class");
	}

	// TODO: Создать асинхронную таску на пополнение токенов
	private static ServerTask task;

	private static final Map<UUID, Integer> uuidToPat = new HashMap<>();

	public static int getAvailablePats(UUID uuid) {
		return uuidToPat.getOrDefault(uuid, PatPatServerConfig.getInstance().getRateLimitConfig().getTokenLimit());
	}

	public static boolean canPat(UUID uuid) {
		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
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
		if (task != null) {
			return;
		}
		RateLimitConfig config = PatPatServerConfig.getInstance().getRateLimitConfig();
		if (!config.isEnabled()) {
			return;
		}
		Time configInterval = config.getTokenIncrementInterval();
		long period = configInterval.getValue() * configInterval.getUnit().getMultiplier() * 20L;
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(
				PatPatPlugin.getInstance(),
				() -> RateLimitManager.addPats(config.getTokenIncrement()),
				0,
				period
		);
	}

	public static void reloadTask() {
		closeTask();
		runTask();
	}

	public static void closeTask() {
		if (task == null) {
			return;
		}
		task.stop();
		task = null;
	}
}
