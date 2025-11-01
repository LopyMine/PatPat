package net.lopymine.patpat.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.wolf.Wolf;

import java.util.Random;

public class TameUtils {
    private static final Random random = new Random();
    private static boolean patChanceDropped() {
        return random.nextInt(100) < 2;
    }

    public static void runByChance(Entity entity, ServerPlayer player) {
        if (entity instanceof Wolf wolf) {
            if (!wolf.isTame() && patChanceDropped()) {
                wolf.tame(player);
                wolf.setOwner(player);
            }
        }
    }
}

