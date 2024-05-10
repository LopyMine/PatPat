package net.lopymine.patpat;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.*;

import net.lopymine.patpat.packet.*;

import java.util.*;

public class PatPat implements ModInitializer {

    public static final String MOD_ID = "patpat";
    public static final Logger LOGGER = LoggerFactory.getLogger("Patpat");

    private static final Set<UUID> BANNED_PLAYERS = new HashSet<>(); // TODO

    @Override
    public void onInitialize() {
        LOGGER.info("PatPat Initialized");
        ServerPlayNetworking.registerGlobalReceiver(PatEntityC2SPacket.TYPE, (packet, sender, responseSender) -> {
            if (BANNED_PLAYERS.contains(sender.getUuid())) {
                return;
            }
            ServerWorld serverWorld = sender.getServerWorld();
            Entity entity = serverWorld.getEntity(packet.getPatEntityUuid());
            if (entity == null) {
                return;
            }
            ChunkPos chunkPos = sender.getChunkPos();
            for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, chunkPos)) {
                if (player == sender) {
                    continue;
                }
                ServerPlayNetworking.send(player, new PatEntityS2CPacket(packet.getPattingPlayerUuid(), packet.getPatEntityUuid()));
            }
        });
    }

    public static Identifier i(String path) {
        return new Identifier(MOD_ID, path);
    }
}