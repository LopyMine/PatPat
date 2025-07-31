package net.lopymine.patpat.client.config;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.IgnoreMobListConfigUtils;
import net.lopymine.patpat.utils.ProximityPacketServerWhitelistConfigUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ProximityPacketServersWhitelistConfig {

		private static final String FILENAME = "proximity_packet_servers_whitelist.txt";
		private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("ProximityPacketServersWhitelistConfig");
		private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();

		private static net.lopymine.patpat.client.config.IgnoreMobListConfig instance;

		private final Set<String> servers = new HashSet<>();

		public static net.lopymine.patpat.client.config.IgnoreMobListConfig getInstance() {
			if (instance == null) {
				reload();
			}
			return instance;
		}

		public static void reload() {
			net.lopymine.patpat.client.config.IgnoreMobListConfig config = new net.lopymine.patpat.client.config.IgnoreMobListConfig();
			IgnoreMobListConfigUtils.read(CONFIG_FILE, LOGGER, config.getIgnoredMobs());
			instance = config;
		}

		public boolean addServer(@NotNull String address) {
			return servers.add(address);
		}

		public boolean removeServer(@NotNull String address) {
			return servers.remove(address);
		}

		public boolean contains(@NotNull String address) {
			return servers.contains(address);
		}

		public void saveAsync() {
			CompletableFuture.runAsync(this::save);
		}

		public void save() {
			this.save(CONFIG_FILE);
		}

		public void save(File configFile) {
			ProximityPacketServerWhitelistConfigUtils.save(configFile, LOGGER, servers);
		}
}
