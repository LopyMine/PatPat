package net.lopymine.patpat;

import net.fabricmc.api.ModInitializer;

import net.lopymine.patpat.manager.PatPatConfigManager;
import net.lopymine.patpat.manager.server.*;

public class PatPat implements ModInitializer {

	public static final String MOD_VERSION = /*$ mod_version*/ "1.2.0+1.21.4";
	public static final String MOD_NAME = /*$ mod_name*/ "PatPat";
	public static final String MOD_ID = /*$ mod_id*/ "patpat";
	public static final String SERVER_CONFIG_VERSION = /*$ mod_version*/ "1.2.0+1.21.4";

	public static final String ISSUE_LINK = "https://github.com/LopyMine/" + MOD_ID + "/issues";

	public static final PatLogger LOGGER = new PatLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		PatPatConfigManager.onInitialize();
		PatPatConfigManager.reload();
		PatPatServerCommandManager.register();
		PatPatServerPacketManager.register();
		PatPat.LOGGER.info("PatPat Initialized");
		PatPat.LOGGER.debug("Debug mode enabled");
	}
}