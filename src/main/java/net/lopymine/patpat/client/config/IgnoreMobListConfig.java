package net.lopymine.patpat.client.config;

import lombok.Getter;
import net.minecraft.world.entity.EntityType;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.IgnoreMobListConfigUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
public class IgnoreMobListConfig {

	private static final String FILENAME = "ignore_mob_list.txt";
	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("IgnoreMobListConfig");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();

	private static IgnoreMobListConfig instance;

	private final Set<EntityType<?>> ignoredMobs = new HashSet<>();

	public static IgnoreMobListConfig getInstance() {
		if (instance == null) {
			reload();
		}
		return instance;
	}

	public static void reload() {
		IgnoreMobListConfig config = new IgnoreMobListConfig();
		IgnoreMobListConfigUtils.read(CONFIG_FILE, LOGGER, config.getIgnoredMobs());
		instance = config;
	}

	public boolean addMob(@NotNull EntityType<?> type) {
		return ignoredMobs.add(type);
	}

	public boolean removeMob(@NotNull EntityType<?> type) {
		return ignoredMobs.remove(type);
	}

	public boolean isIgnored(@NotNull EntityType<?> type) {
		return ignoredMobs.contains(type);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		this.save(CONFIG_FILE);
	}

	public void save(File configFile) {
		IgnoreMobListConfigUtils.save(configFile, LOGGER, ignoredMobs);
	}
}
