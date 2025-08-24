package net.lopymine.patpat.client.config.list;

import lombok.Getter;
import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.common.config.list.AbstractListConfig;
import org.jetbrains.annotations.*;

import java.io.File;
import java.util.*;

@Getter
public class PatPatClientProxLibServersWhitelistConfig extends AbstractListConfig<String> {

	private static final PatLogger LOGGER = PatPatClient.LOGGER.extend(PatPatClientProxLibServersWhitelistConfig.class.getSimpleName());
	private static final File CONFIG_FILE = PatPatConfigManager.CONFIG_PATH.resolve("proximity_packet_servers_whitelist.txt").toFile();
	private static final PatPatClientProxLibServersWhitelistConfig INSTANCE = new PatPatClientProxLibServersWhitelistConfig();

	public static final List<String> DEFAULT_VALUES = List.of("127.0.0.1", "localhost");

	private final List<String> values = new ArrayList<>();

	public static PatPatClientProxLibServersWhitelistConfig getInstance() {
		return getInitialized(INSTANCE);
	}

	private PatPatClientProxLibServersWhitelistConfig() {
		super(DEFAULT_VALUES, LOGGER, CONFIG_FILE);
	}

	public static void rewriteServersList(List<String> serversList) {
		PatPatClientProxLibServersWhitelistConfig config = PatPatClientProxLibServersWhitelistConfig.getInstance();
		List<String> servers = config.getValues();
		servers.clear();
		servers.addAll(serversList);
		config.save();
	}

	public boolean contains(@NotNull String address) {
		return this.values.contains(address);
	}

	@Override
	protected @Nullable String decode(String line) {
		return line;
	}

	@Override
	protected @Nullable String encode(String element) {
		return element;
	}
}
