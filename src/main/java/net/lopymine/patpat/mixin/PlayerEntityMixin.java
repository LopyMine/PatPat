package net.lopymine.patpat.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.component.*;
import net.lopymine.patpat.hand.PatPatSoundManager;
import net.lopymine.patpat.packet.PatPatC2SSoundEvent;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "interact", at = @At("RETURN"), cancellable = true)
    private void onInteract(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() == ActionResult.PASS) {
            if (hand == Hand.OFF_HAND || !entity.getWorld().isClient) {
                return;
            }
            if (((Object) this) instanceof PlayerEntity player) {
                if (player.isSpectator() || !player.getMainHandStack().getItem().equals(Items.AIR) || player.isDead() || !player.isSneaking()){
                    return;
                }
                PatPatComponent component = entity.getComponent(PatPatComponents.PATPAT_COMPONENT);
                component.pat();
                ClientPlayNetworking.send(new PatPatC2SSoundEvent(entity));
                PatPatSoundManager.playSoundFor(player);
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }
}
