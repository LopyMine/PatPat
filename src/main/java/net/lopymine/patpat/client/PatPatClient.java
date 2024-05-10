package net.lopymine.patpat.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.PatPatSoundManager;
import net.lopymine.patpat.packet.PatEntityS2CPacket;
import net.lopymine.patpat.utils.WorldUtils;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class PatPatClient implements ClientModInitializer {
    private static final Set<UUID> AUTHORS = Set.of(
            UUID.fromString("192e3748-12d5-4573-a8a5-479cd394a1dc"), // LopyMine
            UUID.fromString("7b829ed5-9b74-428f-9b4d-ede06975fbc1") // nikita51
    );

    private static final Set<PatEntity> PAT_ENTITIES = new HashSet<>();
    private static final Set<UUID> BANNED_PLAYERS = new HashSet<>(); // TODO


    @Override
    public void onInitializeClient() {
        PatPat.LOGGER.info("PatPat Client Initialized");
        PatPatSoundManager.onInitialize();

        ClientPlayNetworking.registerGlobalReceiver(PatEntityS2CPacket.TYPE, ((packet, player, responseSender) -> {
            ClientWorld clientWorld = player.clientWorld;
            if (clientWorld == null) {
                return;
            }
            if (BANNED_PLAYERS.contains(packet.getPattingPlayerUuid())) {
                return;
            }
            Entity entity = WorldUtils.getEntity(clientWorld, packet.getPatEntityUuid());
            if (!(entity instanceof LivingEntity livingEntity)) {
                return;
            }
            PatEntity patEntity = PatPatClient.pat(livingEntity, packet.getPattingPlayerUuid());
            PatPatSoundManager.playSoundFor(patEntity, player);
        }));
    }

    @Nullable
    public static PatEntity getPatEntity(LivingEntity entity) {
        for (PatEntity pe : PAT_ENTITIES) {
            if (pe.is(entity)) {
                return pe;
            }
        }
        return null;
    }

    public static void removePatEntity(PatEntity patEntity) {
        PatPatClient.PAT_ENTITIES.remove(patEntity);
    }

    public static PatEntity pat(LivingEntity entity, @Nullable UUID pattingPlayerUuid) {
        PatEntity patEntity = new PatEntity(entity, pattingPlayerUuid != null && AUTHORS.contains(pattingPlayerUuid));
        PatPatClient.PAT_ENTITIES.add(patEntity);
        return patEntity;
    }
}
