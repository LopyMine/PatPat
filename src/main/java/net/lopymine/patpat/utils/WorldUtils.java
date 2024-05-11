package net.lopymine.patpat.utils;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WorldUtils {

	private WorldUtils() {
		throw new IllegalStateException("Utility class");
	}

	@Nullable
	public static Entity getEntity(@NotNull ClientWorld world, @NotNull UUID uuid) {
		for (Entity entity : world.getEntities()) {
			if (entity.getUuid().equals(uuid)) {
				return entity;
			}
		}
		return null;
	}
}
