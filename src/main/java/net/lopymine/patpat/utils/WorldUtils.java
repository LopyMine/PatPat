package net.lopymine.patpat.utils;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import java.util.UUID;
import org.jetbrains.annotations.*;

public class WorldUtils {
    @Nullable
    public static Entity getEntity(@NotNull ClientWorld world, UUID uuid) {
        for (Entity entity : world.getEntities()) {
            if (entity.getUuid().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }
}
