package net.lopymine.patpat.client;

import lombok.Getter;
import org.slf4j.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.manager.client.*;

public class PatPatClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("PatPat/Client");
	@Getter
	private static PatPatClientConfig config;

	@Override
	public void onInitializeClient() {
		PatPatClient.config = PatPatClientConfig.getInstance();
		PatPatClientSoundManager.register();
		PatPatClientCommandManager.register();
		PatPatClientPacketManager.register();

		LOGGER.info("PatPat Client Initialized");
	}
}
