package net.lopymine.patpat.entrypoint.forge;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.dedicated.PatPatDedicatedServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = PatPat.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class PatPatNeoForgeDedicatedEntrypoint {

	private static IEventBus INITIALIZATION_EVENT_BUS;

	public PatPatNeoForgeDedicatedEntrypoint(IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPatDedicatedServer.onInitializeServer();
		INITIALIZATION_EVENT_BUS = null;
	}

	public static IEventBus getEventBus() {
		return INITIALIZATION_EVENT_BUS == null ? NeoForge.EVENT_BUS : INITIALIZATION_EVENT_BUS;
	}

}
