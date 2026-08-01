package net.lopymine.patpat.entrypoint.impl.forge;

//? if forge {

/*import net.lopymine.patpat.PatPat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(PatPat.MOD_ID)
public class ForgeCommonEntrypoint {

	public ForgeCommonEntrypoint() {
		PatPat.onInitialize();

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ForgeClientEntrypoint::onInitializeClient);
		DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> ForgeDedicatedEntrypoint::onInitializeServer);
	}

}
*///?}
