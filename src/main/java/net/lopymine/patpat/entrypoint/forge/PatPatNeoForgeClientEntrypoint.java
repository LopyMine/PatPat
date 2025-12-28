package net.lopymine.patpat.entrypoint.forge;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = PatPat.MOD_ID, dist = Dist.CLIENT)
public class PatPatNeoForgeClientEntrypoint {

	private static IEventBus INITIALIZATION_EVENT_BUS;

	public PatPatNeoForgeClientEntrypoint(IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPatClient.onInitializeClient();
		INITIALIZATION_EVENT_BUS = null;
	}

	public static IEventBus getEventBus() {
		return INITIALIZATION_EVENT_BUS == null ? NeoForge.EVENT_BUS : INITIALIZATION_EVENT_BUS;
	}

}
