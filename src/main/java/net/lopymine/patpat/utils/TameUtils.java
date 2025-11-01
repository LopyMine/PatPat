package net.lopymine.patpat.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;

import java.util.Random;

public class TameUtils {
    private static final boolean showParticles = true;
    private static final Random random = new Random();

    private static boolean tameChanceDropped(double chance) {
        return random.nextDouble(100) < chance;
    }

    public static void runByChance(Entity entity, ServerPlayer player, ServerLevel serverWorld) {

        if (entity instanceof Wolf wolf) {
            if (!wolf.isTame() && tameChanceDropped(wolf.isAngry() ? 1 : 2)) {
                wolf.tame(player);
                wolf.setOwner(player);
                wolf.stopBeingAngry(); // Because sounds are still appearing
                showParticles(wolf, serverWorld);
            }
        }


        if (entity instanceof Cat cat) {
            // It seems to me that in real life cats will be much less likely to become attached to a person if they are simply petted, but this is purely my opinion!

            if (!cat.isTame() && tameChanceDropped(1.5)) {
                cat.tame(player);
                cat.setOwner(player);
                showParticles(cat, serverWorld);
            }
        }
    }

    private static void showParticles(Entity entity, ServerLevel serverWorld) {
        if (!showParticles) { return; }

        serverWorld.sendParticles(
                ParticleTypes.HEART,
                entity.getX(),
                entity.getY() + 1,
                entity.getZ(),
                5,
                entity.getRandom().nextGaussian() * 0.02,
                0.5,
                entity.getRandom().nextGaussian() * 0.02,
                1.0
        );
    }
}

