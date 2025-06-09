package net.lopymine.patpat.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.manager.*;
import net.lopymine.patpat.client.packet.*;
import net.lopymine.patpat.client.resourcepack.PatPatClientSoundManager;
import net.lopymine.patpat.compat.replaymod.ReplayModCompat;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.resourcepack.PlayerConfig;
import net.lopymine.patpat.entity.PatEntity;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

//? <=1.19.3 {
/*import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
*///?}

import net.lopymine.patpat.compat.flashback.FlashbackCompat;
import net.lopymine.patpat.packet.PatPacket;
import net.lopymine.patpat.packet.c2s.PatEntityC2SPacket;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

	@Shadow
	@Nullable
	public HitResult hitResult;

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Shadow
	private int rightClickDelay;

	@Shadow
	public abstract
		//? >=1.20.2 {
	net.minecraft.client.User
	//?} else {
	/*net.minecraft.client.util.Session
	 *///?}
	getUser();

	@Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
	private void onRightClickMouse(CallbackInfo ci) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		if (!config.getMainConfig().isModEnabled()) {
			return;
		}
		if (!(this.hitResult instanceof EntityHitResult result)) {
			return;
		}
		if (this.player == null || this.player.isDeadOrDying()) {
			return;
		}
		Entity entity = result.getEntity();
		if (!(entity instanceof LivingEntity livingEntity)
				|| this.player.isSpectator()
				|| !this.player.getMainHandItem().isEmpty()
				|| !this.player.isShiftKeyDown()) {
			return;
		}

		if (livingEntity.isInvisible()) {
			return;
		}

		PatPacket<ServerLevel, ?> packet = PatPatClientPacketManager.getPatPacket(livingEntity);
		PatPatClientNetworkManager.sendPacketToServer(packet);

		UUID currentUuid = this.getUser()/*? >=1.20 {*/.getProfileId()/*?} else {*//*.getProfile().getId()*//*?}*/;
		PlayerConfig whoPatted = PlayerConfig.of(this.getUser().getName(), currentUuid);

		PatEntity patEntity = PatPatClientManager.pat(livingEntity, whoPatted);

		if (config.getVisualConfig().isSwingHandEnabled()) {
			this.player.swing(InteractionHand.MAIN_HAND);
		}

		ReplayModCompat.onPat(livingEntity.getId(), this.player.getId());
		FlashbackCompat.onPat(livingEntity.getId(), this.player.getId());
		PatPatClientProxLibPacketManager.onPat(livingEntity.getId());

		if (config.getSoundsConfig().isSoundsEnabled()) {
			PatPatClientSoundManager.playSound(patEntity, this.player, config.getSoundsConfig().getSoundsVolume());
		}

		this.rightClickDelay = 4;
		ci.cancel();
	}
}
