package net.lopymine.patpat.compat.iris;

//? if iris {

//? if >=1.18.1 {
import net.irisshaders.iris.api.v0.IrisApi;
//?} else {
/*import net.coderbot.iris.shadows.ShadowRenderingState;
*///?}

import net.lopymine.patpat.compat.LoadedMods;

public class IrisCompat {

	public static boolean isRenderingShadowPass() {
		if (!LoadedMods.IRIS_LOADED) {
			return false;
		}
		//? if >=1.18.1 {
		return IrisApi.getInstance().isRenderingShadowPass();
		 //?} else {
		/*return ShadowRenderingState.areShadowsCurrentlyBeingRendered();
		*///?}
	}

}

//?} else {
/*import net.lopymine.patpat.compat.LoadedMods;

public class IrisCompat {

	public static boolean isRenderingShadowPass() {
		return false;
	}

}
*///?}
