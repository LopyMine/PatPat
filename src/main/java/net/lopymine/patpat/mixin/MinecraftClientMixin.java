package net.lopymine.patpat.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.PatPatSoundManager;
import net.lopymine.patpat.packet.PatEntityC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.session.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow
	@Nullable
	public HitResult crosshairTarget;

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Shadow
	private int itemUseCooldown;

	@Shadow
	public abstract Session getSession();

	@Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
	private void onRightClickMouse(CallbackInfo ci) {
		if (!(this.crosshairTarget instanceof EntityHitResult hitResult)) {
			return;
		}
		if (this.player == null || this.player.isDead()) {
			return;
		}
		Entity entity = hitResult.getEntity();
		if (!(entity instanceof LivingEntity livingEntity)
			|| this.player.isSpectator()
			|| !this.player.getMainHandStack().isEmpty()
			|| !this.player.isSneaking()) {
			return;
		}

		ClientPlayNetworking.send(new PatEntityC2SPacket(this.player, entity));
		PatEntity patEntity = PatPatClient.pat(livingEntity, this.getSession().getUuidOrNull());
		PatPatSoundManager.playSound(patEntity, this.player);
		this.itemUseCooldown = 4;
		ci.cancel();
	}
}
