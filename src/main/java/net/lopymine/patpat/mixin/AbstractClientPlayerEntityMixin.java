package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.RLUtils;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin {

	@Unique
	private static final Identifier PATPAT_CAPE_ID = RLUtils.modId("textures/cape/patpat_cape_hand.png");

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getSkin()Lnet/minecraft/world/entity/player/PlayerSkin;"), method = "getSkin")
	private PlayerSkin customCape(PlayerInfo instance, Operation<PlayerSkin> original) {
		PlayerSkin call = original.call(instance);
		ClientAsset.Texture capeTexture = call.cape();
		if (!PatPatClient.SECOND_AUTHOR_UUID.equals(instance.getProfile().id()) && (capeTexture != null || !PatPatClient.AUTHORS.contains(instance.getProfile().id()))) {
			return call;
		}
		return new PlayerSkin(call.body(), new ResourceTexture(PATPAT_CAPE_ID, PATPAT_CAPE_ID), call.elytra(), call.model(), call.secure());
	}
}