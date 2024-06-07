package net.lopymine.patpat.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.session.Session;
import net.minecraft.entity.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.PlayerConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.*;
import net.lopymine.patpat.packet.PatEntityC2SPacket;

import org.jetbrains.annotations.Nullable;

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
		PatPatClientConfig config = PatPatClient.getConfig();
		if (!config.isModEnabled()) {
			return;
		}
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

		boolean donor = PatPatClientDonorManager.getInstance().isAmDonor() && config.isUseDonorAnimationEnabled();
		ClientPlayNetworking.send(new PatEntityC2SPacket(livingEntity, donor));

		Session session = this.getSession();
		PlayerConfig whoPatted = PlayerConfig.of(session.getUsername(), session.getUuidOrNull());
		PatEntity patEntity = PatPatClientManager.pat(livingEntity, whoPatted, donor);

		if (config.isSoundsEnabled()) {
			PatPatClientSoundManager.playSound(patEntity, this.player, config.getSoundsVolume());
		}
		if (config.isSwingHandEnabled()) {
			this.player.swingHand(Hand.MAIN_HAND);
		}

		this.itemUseCooldown = 4;
		ci.cancel();
	}
}
