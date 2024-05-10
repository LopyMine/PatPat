package net.lopymine.patpat.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.entity.*;
import net.lopymine.patpat.manager.PatPatSoundManager;
import net.lopymine.patpat.packet.PatEntityS2CPacket;
import net.lopymine.patpat.utils.WorldUtils;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class PatPatClient implements ClientModInitializer {
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
            if (entity == null) {
                return;
            }
            PatEntity patEntity = PatPatClient.addPatEntity(entity);
            PatPatSoundManager.playSoundFor(patEntity.getEntity());
        }));
    }

    @Nullable
    public static PatEntity getPatEntity(Entity entity) {
        for (PatEntity pe : PAT_ENTITIES) {
            if (pe.is(entity)) {
                return pe;
            }
        }
        return null;
    }

    public static boolean removePatEntity(PatEntity patEntity) {
        return PatPatClient.PAT_ENTITIES.remove(patEntity);
    }

    public static PatEntity addPatEntity(Entity entity) {
        PatEntity patEntity = new PatEntity(entity);
        PatPatClient.PAT_ENTITIES.add(patEntity);
        return patEntity;
    }
}
