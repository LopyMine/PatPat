package net.lopymine.patpat.entrypoint.forge;

import net.lopymine.patpat.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(PatPat.MOD_ID)
public class PatPatNeoForgeCommonEntrypoint {

	private static IEventBus INITIALIZATION_EVENT_BUS;

	public PatPatNeoForgeCommonEntrypoint(IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPat.onInitialize();
		INITIALIZATION_EVENT_BUS = null;
	}

	public static IEventBus getEventBus() {
		return INITIALIZATION_EVENT_BUS == null ? NeoForge.EVENT_BUS : INITIALIZATION_EVENT_BUS;
	}

}
