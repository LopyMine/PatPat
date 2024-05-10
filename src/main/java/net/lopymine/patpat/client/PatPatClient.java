package net.lopymine.patpat.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.hand.PatPatSoundManager;
import net.lopymine.patpat.packet.PatPatS2CSoundEvent;

public class PatPatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PatPat.LOGGER.info("PatPat Client Initialized");
        PatPatSoundManager.onInitialize();

        ClientPlayNetworking.registerGlobalReceiver(PatPatS2CSoundEvent.TYPE, ((packet, player, responseSender) -> {
            ClientWorld clientWorld = player.clientWorld;
            if (clientWorld == null) {
                return;
            }
            Entity entity = clientWorld.getEntityById(packet.getEntityID());
            PatPatSoundManager.playSoundFor(entity);
        }));
    }
}
