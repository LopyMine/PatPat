package net.lopymine.patpat.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.entity.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.compat.replaymod.ReplayModCompat;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.config.resourcepack.PlayerConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.*;
import net.lopymine.patpat.packet.*;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

//? <=1.19.3 {
/*import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
*///?}

import net.lopymine.patpat.compat.flashback.FlashbackCompat;

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
	public abstract
		//? >=1.20.2 {
	net.minecraft.client.session.Session
	//?} else {
	/*net.minecraft.client.util.Session
	 *///?}
	getSession();

	@Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
	private void onRightClickMouse(CallbackInfo ci) {
		PatPatClientConfig config = PatPatClient.getConfig();
		if (!config.getMainConfig().isModEnabled()) {
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

		if (livingEntity.isInvisible()) {
			return;
		}

		PatEntityC2SPacket packet = new PatEntityC2SPacket(livingEntity);
		//? >=1.19.4 {
		ClientPlayNetworking.send(packet);
		//?} else {
		/*PacketByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		ClientPlayNetworking.send(PatEntityC2SPacket.PACKET_ID, buf);
		*///?}

		UUID currentUuid = this.getSession()/*? >=1.20 {*/.getUuidOrNull()/*?} else {*//*.getProfile().getId()*//*?}*/;
		PlayerConfig whoPatted = PlayerConfig.of(this.getSession().getUsername(), currentUuid);

		PatEntity patEntity = PatPatClientManager.pat(livingEntity, whoPatted);

		if (config.getVisualConfig().isSwingHandEnabled()) {
			this.player.swingHand(Hand.MAIN_HAND);
		}

		ReplayModCompat.onPat(livingEntity.getUuid(), currentUuid);
		FlashbackCompat.onPat(livingEntity.getUuid(), currentUuid);
		PatPatProxLibPacketManager.onPat(packet.getPattedEntityUuid());

		if (config.getSoundsConfig().isSoundsEnabled()) {
			PatPatClientSoundManager.playSound(patEntity, this.player, config.getSoundsConfig().getSoundsVolume());
		}

		this.itemUseCooldown = 4;
		ci.cancel();
	}
}
