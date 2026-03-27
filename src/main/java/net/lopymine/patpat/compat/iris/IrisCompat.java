package net.lopymine.patpat.compat.iris;

import net.irisshaders.iris.api.v0.IrisApi;
import net.lopymine.patpat.compat.LoadedMods;

public class IrisCompat {

	public static boolean isRenderingShadowPass() {
		if (!LoadedMods.IRIS_LOADED) {
			return false;
		}

		//? if iris {
		return IrisApi.getInstance().isRenderingShadowPass();
		//?} else {
		/*return false;
		*///?}
	}

}
