package net.lopymine.patpat.utils;

import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.*;

public class WorldUtils {

	private WorldUtils() {
		throw new IllegalStateException("Utility class");
	}

	@Nullable
	public static Entity getEntity(@NotNull ClientLevel world, @NotNull UUID uuid) {
		for (Entity entity : world.entitiesForRendering()) {
			if (entity.getUUID().equals(uuid)) {
				return entity;
			}
		}
		return null;
	}
}
