package net.lopymine.patpat.entrypoint.impl.neoforge;

//? if neoforge {
/*import net.lopymine.patpat.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

//? if <=1.20.4 {
import net.lopymine.patpat.entrypoint.impl.neoforge.loader.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.ModContainer;
//?}

@Mod(PatPat.MOD_ID)
public class NeoForgeCommonEntrypoint {

	private static IEventBus INITIALIZATION_EVENT_BUS;

	//? if >=1.20.5 {
	/^public NeoForgeCommonEntrypoint(IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPat.onInitialize();
		INITIALIZATION_EVENT_BUS = null;
	}
	^///?} else {
	public NeoForgeCommonEntrypoint(ModContainer container, IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPat.onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new NeoForgeClientEntrypoint(container, bus));
		DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> new NeoForgeDedicatedEntrypoint(bus));
		INITIALIZATION_EVENT_BUS = null;
	}
	//?}

	public static IEventBus getEventBus() {
		return INITIALIZATION_EVENT_BUS == null ? NeoForge.EVENT_BUS : INITIALIZATION_EVENT_BUS;
	}

}
*///?}
