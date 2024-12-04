package net.lopymine.patpat;

import lombok.Getter;

import net.fabricmc.api.ModInitializer;

import net.lopymine.patpat.config.server.PatPatServerConfig;
import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.manager.server.*;

public class PatPat implements ModInitializer {

	public static final String MOD_VERSION = /*$ mod_version*/ "1.0.1";
	public static final String MOD_NAME = /*$ mod_name*/ "PatPat";
	public static final String MOD_ID = /*$ mod_id*/ "patpat";

	public static final PatLogger LOGGER = new PatLogger(MOD_NAME);
	@Getter
	private static PatPatServerConfig config;

	@Override
	public void onInitialize() {
		PatPatConfigManager.onInitialize();
		PatPat.config = PatPatServerConfig.getInstance();
		PatPatServerCommandManager.register();
		PatPatServerPacketManager.register();
		PatPat.LOGGER.info("PatPat Initialized");
	}
}