package net.lopymine.patpat.entrypoint.forge;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.modmenu.integration.PatPatModMenuIntegration;
import net.minecraftforge.fml.ModLoadingContext;

public class ForgeClientEntrypoint {

	public static void onInitializeClient() {
		PatPatClient.onInitializeClient();

		PatPatModMenuIntegration integration = new PatPatModMenuIntegration();
		integration.register(ModLoadingContext.get().getActiveContainer());
	}

}
