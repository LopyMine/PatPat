package net.lopymine.patpat.mixin;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

//? if >=1.21.9 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.ClientAsset.*;
//?} elif >1.20.1 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.resources.PlayerSkin;
*///?} else {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jetbrains.annotations.Nullable;
*///?}

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.RLUtils;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin {

	@Unique
	private static final Identifier PATPAT_CAPE_ID = RLUtils.modId("textures/cape/patpat_cape_hand.png");

	//? if >=1.21.9 {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getSkin()Lnet/minecraft/world/entity/player/PlayerSkin;"), method = "getSkin")
	private PlayerSkin customCape(PlayerInfo instance, Operation<PlayerSkin> original) {
		PlayerSkin call = original.call(instance);
		ClientAsset.Texture capeTexture = call.cape();
		if (!PatPatClient.SECOND_AUTHOR_UUID.equals(instance.getProfile().id()) && (capeTexture != null || !PatPatClient.AUTHORS.contains(instance.getProfile().id()))) {
			return call;
		}
		return new PlayerSkin(call.body(), new ResourceTexture(PATPAT_CAPE_ID, PATPAT_CAPE_ID), call.elytra(), call.model(), call.secure());
	}
	//?} elif >1.20.1 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getSkin()Lnet/minecraft/client/resources/PlayerSkin;"), method = "getSkin")
	private PlayerSkin customCape(PlayerInfo instance, Operation<PlayerSkin> original) {
		PlayerSkin call = original.call(instance);
		Identifier capeTexture = call.capeTexture();

		if (!PatPatClient.SECOND_AUTHOR_UUID.equals(instance.getProfile().getId()) && (capeTexture != null || !PatPatClient.AUTHORS.contains(instance.getProfile().getId()))) {
			return call;
		}
		return new PlayerSkin(call.texture(), call.textureUrl(), PATPAT_CAPE_ID, call.elytraTexture(), call.model(), call.secure());
	}
	*///?} else {


	/*@Shadow @Nullable protected abstract PlayerInfo getPlayerInfo();

	@Inject(method = "getCloakTextureLocation", at = @At("RETURN"), cancellable = true)
	private void customCape(CallbackInfoReturnable<Identifier> cir) {
		PlayerInfo playerListEntry = this.getPlayerInfo();
		if (playerListEntry == null) {
			return;
		}

		if (PatPatClient.SECOND_AUTHOR_UUID.equals(playerListEntry.getProfile().getId())) {
			cir.setReturnValue(PATPAT_CAPE_ID);
		}

		Identifier original = cir.getReturnValue();
		if (original != null) {
			return;
		}

		if (PatPatClient.AUTHORS.contains(playerListEntry.getProfile().getId())) {
			cir.setReturnValue(PATPAT_CAPE_ID);
		}
	}
	*///?}
}