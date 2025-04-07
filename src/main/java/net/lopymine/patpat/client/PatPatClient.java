package net.lopymine.patpat.client;

import lombok.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.manager.client.*;

import java.util.*;

public class PatPatClient implements ClientModInitializer {

	public static final PatLogger LOGGER = new PatLogger("PatPat/Client");
	public static final Set<UUID> AUTHORS_UUIDS = Set.of(UUID.fromString("7b829ed5-9b74-428f-9b4d-ede06975fbc1"), UUID.fromString("192e3748-12d5-4573-a8a5-479cd394a1dc"));
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
		PatPatClientManager.register();

		LOGGER.info("PatPat Client Initialized");
	}
}
