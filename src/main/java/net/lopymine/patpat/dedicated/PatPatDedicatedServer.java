package net.lopymine.patpat.dedicated;

import net.lopymine.patpat.logger.PatLogger;

public class PatPatDedicatedServer {

	public static final PatLogger LOGGER = new PatLogger("PatPat/Dedicated");

	public static void onInitializeServer() {
		PatPatDedicatedServerTranslationManager.reload();
	}
}
