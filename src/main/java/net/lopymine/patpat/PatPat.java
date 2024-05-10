package net.lopymine.patpat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.packet.*;

public class PatPat implements ModInitializer {

    public static final String MOD_ID = "patpat";
    public static final Logger LOGGER = LoggerFactory.getLogger("Patpat");

    @Override
    public void onInitialize() {
        LOGGER.info("PatPat Initialized");
        ServerPlayNetworking.registerGlobalReceiver(PatPatC2SSoundEvent.TYPE, (packet, sender, responseSender) -> {
            ServerWorld serverWorld = sender.getServerWorld();
            ChunkPos chunkPos = sender.getChunkPos();
            for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
                if (player == sender) {
                    continue;
                }
                ServerPlayNetworking.send(player, new PatPatS2CSoundEvent(packet));
            }
        });
    }

    public static Identifier i(String path) {
        return new Identifier(MOD_ID, path);
    }
}