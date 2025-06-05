package net.lopymine.patpat.mixin;


import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

//? >1.20.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;

import com.mojang.authlib.GameProfile;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.IdentifierUtils;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin {

	//? >1.20.1 {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getSkin()Lnet/minecraft/client/resources/PlayerSkin;"), method = "getSkin")
	private PlayerSkin generated(PlayerInfo instance, Operation<PlayerSkin> original) {
		PlayerSkin call = original.call(instance);
		ResourceLocation capeTexture = call.capeTexture();
		if (capeTexture != null || !PatPatClient.AUTHORS.contains(instance.getProfile().getId())) {
			return call;
		}
		ResourceLocation patpatCapeTexture = IdentifierUtils.textureId("cape/patpat_cape_hand.png");
		return new PlayerSkin(call.texture(), call.textureUrl(), patpatCapeTexture, call.elytraTexture(), call.model(), call.secure());
	}
	//?} else {
	/*@Shadow
	@Nullable
	protected abstract PlayerListEntry getPlayerListEntry();

	@Inject(method = "getCapeTexture", at = @At("RETURN"), cancellable = true)
	private void test(CallbackInfoReturnable<Identifier> cir) {
		Identifier original = cir.getReturnValue();
		if (original != null) {
			return;
		}
		PlayerListEntry playerListEntry = getPlayerListEntry();
		if (playerListEntry == null) {
			return;
		}
		if (PatPatClient.AUTHORS_UUIDS.contains(playerListEntry.getProfile().getId())) {
			Identifier identifier = IdentifierUtils.textureId("cape/patpat_cape_hand.png");
			cir.setReturnValue(identifier);
		}
	}
	*///?}
}
