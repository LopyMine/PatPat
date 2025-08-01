package net.lopymine.patpat.client.config;

import lombok.Getter;
import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.utils.ProximityPacketServersWhitelistConfigUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ProximityPacketServersWhitelistConfig {

	private static final String FILENAME = "proximity_packet_servers_whitelist.txt";
	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend("ProximityPacketServersWhitelistConfig");
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve(FILENAME).toFile();

	public static final List<String> DEFAULT_VALUES = List.of("127.0.0.1", "localhost");

	private static ProximityPacketServersWhitelistConfig instance;

	@Getter
	private final List<String> servers = new ArrayList<>();

	public static ProximityPacketServersWhitelistConfig getInstance() {
		if (instance == null) {
			reload();
		}
		return instance;
	}

	public static void reload() {
		ProximityPacketServersWhitelistConfig config = new ProximityPacketServersWhitelistConfig();
		ProximityPacketServersWhitelistConfigUtils.read(CONFIG_FILE, LOGGER, config.getServers());
		instance = config;
	}

	public static void rewriteServersList(List<String> serversList) {
		ProximityPacketServersWhitelistConfig serverWhitelistConfig = ProximityPacketServersWhitelistConfig.getInstance();
		List<String> servers = serverWhitelistConfig.getServers();
		servers.clear();
		servers.addAll(serversList);
		serverWhitelistConfig.save();
	}

	public boolean addServer(@NotNull String address) {
		return servers.add(address);
	}

	public boolean removeServer(@NotNull String address) {
		return servers.remove(address);
	}

	public void clear() {
		servers.clear();
	}

	public void addAll(Collection<? extends String> servers_ip) {
		servers.addAll(servers_ip);
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
		ProximityPacketServersWhitelistConfigUtils.save(configFile, LOGGER, servers);
	}
}
