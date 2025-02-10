package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.network.*;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.utils.IdentifierUtils;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/PlayerListEntry;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;"), method = "getSkinTextures")
	private SkinTextures generated(PlayerListEntry instance, Operation<SkinTextures> original) {
		SkinTextures call = original.call(instance);
		Identifier capeTexture = call.capeTexture();
		if (capeTexture != null || !PatPatClient.AUTHORS_UUIDS.contains(instance.getProfile().getId())) {
			return call;
		}
		Identifier patpatCapeTexture = IdentifierUtils.textureId("cape/patpat_cape_hand.png");
		return new SkinTextures(call.texture(), call.textureUrl(), patpatCapeTexture, call.elytraTexture(), call.model(), call.secure());
	}

}
