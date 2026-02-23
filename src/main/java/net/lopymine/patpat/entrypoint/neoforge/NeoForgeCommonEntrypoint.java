package net.lopymine.patpat.entrypoint.neoforge;

//? if neoforge {
/*import net.lopymine.patpat.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(PatPat.MOD_ID)
public class NeoForgeCommonEntrypoint {

	private static IEventBus INITIALIZATION_EVENT_BUS;

	public NeoForgeCommonEntrypoint(IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPat.onInitialize();
		INITIALIZATION_EVENT_BUS = null;
	}

	public static IEventBus getEventBus() {
		return INITIALIZATION_EVENT_BUS == null ? NeoForge.EVENT_BUS : INITIALIZATION_EVENT_BUS;
	}

}
*///?}
