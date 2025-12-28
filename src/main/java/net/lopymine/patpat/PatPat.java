package net.lopymine.patpat;

import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.entrypoint.MultiLoader;
import net.lopymine.patpat.logger.PatLogger;
import net.lopymine.patpat.server.command.PatPatServerCommandManager;
import net.lopymine.patpat.server.event.PatPatServerPlayerEvents;
import net.lopymine.patpat.server.packet.PatPatServerPacketManager;

public class PatPat {

	public static final String MOD_VERSION = /*$ mod_version*/ "1.2.4+1.21.11+neoforge";
	public static final String BUILD_CODE_TIME = /*$ build_code_time*/ "01f9f47";
	public static final String MOD_NAME = /*$ mod_name*/ "PatPat";
	public static final String MOD_ID = /*$ mod_id*/ "patpat";
	public static final String SERVER_CONFIG_VERSION = /*$ server_config_version*/ "1.0.0";

	public static final String ISSUE_LINK = "https://github.com/LopyMine/" + MOD_ID + "/issues";

	public static final PatLogger LOGGER = new PatLogger(MOD_NAME);

	public static void onInitialize() {
		PatPatConfigManager.onInitialize();
		PatPatConfigManager.reloadServer();
		PatPatServerCommandManager.register();
		PatPatServerPlayerEvents.register();
		MultiLoader.getInstance().registerServerPackets(PatPatServerPacketManager::register);

		PatPat.LOGGER.info("PatPat Initialized");
		PatPat.LOGGER.debug("Debug Mode Enabled");
	}
}