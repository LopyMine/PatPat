package net.lopymine.patpat.client;

import lombok.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.manager.client.*;

public class PatPatClient implements ClientModInitializer {

	public static final PatLogger LOGGER = new PatLogger("PatPat/Client");
	@Getter
	@Setter
	private static PatPatClientConfig config;

	@Override
	public void onInitializeClient() {
		PatPatClient.config = PatPatClientConfig.getInstance();
		PatPatClientSoundManager.register();
		PatPatClientCommandManager.register();
		PatPatClientPacketManager.register();
		PatPatClientReloadListener.register();
		LoadedMods.onInitialize();

		LOGGER.info("PatPat Client Initialized");
	}
}
