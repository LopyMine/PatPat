package net.lopymine.patpat.compat.flashback;

//? flashback {
import com.moulberry.flashback.Flashback;
import net.lopymine.patpat.compat.LoadedMods;
//?}

public class FlashbackManager {

	public static boolean isInReplay() {
		//? flashback {
		if (!LoadedMods.FLASHBACK_MOD_LOADED) {
			return false;
		}
		return Flashback.isInReplay();
		//?} else {
		/*return false;
		*///?}
	}

}
