package net.lopymine.patpat.dedicated;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.lopymine.patpat.*;

public class PatPatDedicatedServer implements DedicatedServerModInitializer {

	public static final PatLogger LOGGER = new PatLogger("PatPat/Dedicated");

	@Override
	public void onInitializeServer() {
		PatPatDedicatedServerTranslationManager.reload();
	}
}
