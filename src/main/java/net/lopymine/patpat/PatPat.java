package net.lopymine.patpat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import net.fabricmc.api.ModInitializer;

import net.lopymine.patpat.common.config.PatPatConfigManager;
import net.lopymine.patpat.server.command.PatPatServerCommandManager;
import net.lopymine.patpat.server.event.PatPatServerPlayerEvents;
import net.lopymine.patpat.server.packet.PatPatServerPacketManager;
import net.lopymine.patpat.utils.TextUtils;

public class PatPat implements ModInitializer {

	public static final String MOD_VERSION = /*$ mod_version*/ "1.2.0+1.21.4";
	public static final String MOD_NAME = /*$ mod_name*/ "PatPat";
	public static final String MOD_ID = /*$ mod_id*/ "patpat";
	public static final String SERVER_CONFIG_VERSION = /*$ server_config_version*/ "1.0.0";

	public static final String ISSUE_LINK = "https://github.com/LopyMine/" + MOD_ID + "/issues";

	public static final PatLogger LOGGER = new PatLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		PatPatConfigManager.onInitialize();
		PatPatConfigManager.reloadServer();
		PatPatServerCommandManager.register();
		PatPatServerPlayerEvents.register();
		PatPatServerPacketManager.register();
		PatPat.LOGGER.info("PatPat Initialized");
		PatPat.LOGGER.debug("Debug mode enabled");
	}
}