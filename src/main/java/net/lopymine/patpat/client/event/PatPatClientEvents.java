package net.lopymine.patpat.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.lopymine.patpat.manager.client.PatPatClientManager;

public class PatPatClientEvents {

	public static void register() {
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			//? >1.20.2 {
			if (client.getTickManager().isFrozen()) {
				return;
			}
			//?}
			PatPatClientManager.tickEntities();
		});
	}

}
