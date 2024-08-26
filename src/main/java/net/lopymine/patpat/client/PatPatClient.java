package net.lopymine.patpat.client;

import lombok.*;

//? >=1.17
import org.slf4j.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.patpat.compat.LoadedMods;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.manager.client.*;

public class PatPatClient implements ClientModInitializer {

	//? >=1.17
	public static final Logger LOGGER = LoggerFactory.getLogger("PatPat/Client");
	@Getter
	@Setter
	private static PatPatClientConfig config;

	public static void info(String text, Object... args) {
		//? >=1.17 {
		LOGGER.info(text, args);
		//?} else {
		/*System.out.println("[PatPat/Client/INFO] " + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public static void warn(String text, Object... args) {
		//? >=1.17 {
		LOGGER.warn(text, args);
		//?} else {
		/*System.out.println("[PatPat/Client/WARN] " + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	public static void error(String text, Object... args) {
		//? >=1.17 {
		LOGGER.error(text, args);
		//?} else {
		/*System.out.println("[PatPat/Client/ERROR] " + text.replace("{}", "%s").formatted(args));
		 *///?}
	}

	@Override
	public void onInitializeClient() {
		PatPatClient.config = PatPatClientConfig.getInstance();
		PatPatClientSoundManager.register();
		PatPatClientCommandManager.register();
		PatPatClientPacketManager.register();
		PatPatClientReloadListener.register();
		LoadedMods.onInitialize();

		info("PatPat Client Initialized");
	}
}
