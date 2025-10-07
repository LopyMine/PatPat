package net.lopymine.patpat.modmenu.bridge;

//? if >=1.20.1 {
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import net.lopymine.patpat.modmenu.bridge.YACLBridge;

class YACLBridgeTest {

	@Test
	void testYACLScreenCreate() {
		Assertions.assertNotNull(YACLBridge.getScreen(null));
	}

}
//?}