package net.lopymine.patpat.mixin;


import net.minecraft.client.network.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

//? >1.20.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.util.SkinTextures;
//?} else {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jetbrains.annotations.Nullable;
*///?}

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.utils.IdentifierUtils;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {

	//? >1.20.1 {
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/PlayerListEntry;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;"), method = "getSkinTextures")
	private SkinTextures generated(PlayerListEntry instance, Operation<SkinTextures> original) {
		SkinTextures call = original.call(instance);
		Identifier capeTexture = call.capeTexture();
		if (capeTexture != null || !PatPatClient.AUTHORS.contains(instance.getProfile().getId())) {
			return call;
		}
		Identifier patpatCapeTexture = IdentifierUtils.textureId("cape/patpat_cape_hand.png");
		return new SkinTextures(call.texture(), call.textureUrl(), patpatCapeTexture, call.elytraTexture(), call.model(), call.secure());
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
