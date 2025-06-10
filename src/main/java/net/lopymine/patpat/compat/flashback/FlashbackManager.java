package net.lopymine.patpat.compat.flashback;

//? flashback {
import com.moulberry.flashback.Flashback;
//?}

public class FlashbackManager {

	public static boolean isInReplay() {
		//? flashback {
		return Flashback.isInReplay();
		//?} else {
		/*return false;
		*///?}
	}

}
