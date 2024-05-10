package net.lopymine.patpat.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.manager.PatPatSoundManager;
import net.lopymine.patpat.packet.PatEntityC2SPacket;

import org.jetbrains.annotations.Nullable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow private int itemUseCooldown;

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void onRightClickMouse(CallbackInfo ci) {
        if (!(this.crosshairTarget instanceof EntityHitResult hitResult)) {
            return;
        }
        if (this.player == null) {
            return;
        }
        Entity entity = hitResult.getEntity();
        if (this.player.isSpectator() || !this.player.getMainHandStack().getItem().equals(Items.AIR) || this.player.isDead() || !this.player.isSneaking()) {
            return;
        }
        ClientPlayNetworking.send(new PatEntityC2SPacket(this.player, entity));
        PatPatClient.addPatEntity(entity);
        PatPatSoundManager.playSoundFor(this.player);
        this.itemUseCooldown = 4;
        ci.cancel();
    }
}
