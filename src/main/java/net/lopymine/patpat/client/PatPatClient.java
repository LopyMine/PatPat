package net.lopymine.patpat.client;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.patpat.PatLogger;
import net.lopymine.patpat.client.command.PatPatClientCommandManager;
import net.lopymine.patpat.client.event.PatPatClientPlayerEvents;
import net.lopymine.patpat.client.render.*;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.client.resourcepack.*;
import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.compat.LoadedMods;

import java.util.*;

public class PatPatClient implements ClientModInitializer {

	public static final PatLogger LOGGER = new PatLogger("PatPat/Client");
	public static final Set<UUID> AUTHORS = Set.of(UUID.fromString("7b829ed5-9b74-428f-9b4d-ede06975fbc1"), UUID.fromString("192e3748-12d5-4573-a8a5-479cd394a1dc"));
	public static final String CLIENT_CONFIG_VERSION = /*$ client_config_version*/ "1.0.0";

	@Override
	public void onInitializeClient() {
		PatPatConfigManager.reloadClient();
		LoadedMods.onInitialize();
		PatPatClientSoundManager.register();
		PatPatClientCommandManager.register();
		PatPatClientPlayerEvents.register();
		PatPatClientPacketManager.register();
		PatPatClientReloadListener.register();
		PatPatClientProxLibManager.register();
		PatPatClientProxLibPacketManager.register();
		PatPatClientRenderer.register();

		LOGGER.info("PatPat Client Initialized");
	}
}
