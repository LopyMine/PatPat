package net.lopymine.patpat;

import lombok.Getter;
import org.slf4j.*;

import net.fabricmc.api.ModInitializer;

import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.manager.server.*;

public class PatPat implements ModInitializer {
	public static final String MOD_ID = "patpat";
	public static final Logger LOGGER = LoggerFactory.getLogger("PatPat");
	@Getter
	private static PatPatServerConfig config = PatPatServerConfig.getInstance();

	@Override
	public void onInitialize() {
		PatPat.config = PatPatServerConfig.getInstance();
		PatPatConfigManager.onInitialize();
		PatPatServerCommandManager.register();
		PatPatServerPacketManager.register();

		LOGGER.info("PatPat Initialized");
	}
}