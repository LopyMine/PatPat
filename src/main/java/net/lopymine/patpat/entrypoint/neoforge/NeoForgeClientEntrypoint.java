package net.lopymine.patpat.entrypoint.neoforge;

//? if neoforge {
/*import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.modmenu.integration.PatPatModMenuIntegration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = PatPat.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClientEntrypoint {

	private static IEventBus INITIALIZATION_EVENT_BUS;

	public NeoForgeClientEntrypoint(ModContainer container, IEventBus bus) {
		INITIALIZATION_EVENT_BUS = bus;
		PatPatClient.onInitializeClient();

		PatPatModMenuIntegration modMenuIntegration = new PatPatModMenuIntegration();
		modMenuIntegration.register(container);

		INITIALIZATION_EVENT_BUS = null;
	}

	public static IEventBus getEventBus() {
		return INITIALIZATION_EVENT_BUS == null ? NeoForge.EVENT_BUS : INITIALIZATION_EVENT_BUS;
	}

}
*///?}
