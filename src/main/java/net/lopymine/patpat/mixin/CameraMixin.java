package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.PatPatClientManager;

@Mixin(Camera.class)
public class CameraMixin {

	@WrapOperation(at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getStandingEyeHeight()F"), method = "updateEyeHeight")
	private float applyPattingEffect(Entity entity, Operation<Float> original) {
		Float originalHeight = original.call(entity);

		if (!PatPatClient.getConfig().isCameraShackingEnabled()) {
			return originalHeight;
		}

		if (!(entity instanceof LivingEntity livingEntity)) {
			return originalHeight;
		}
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return originalHeight;
		}

		if (PatPatClientManager.expired(patEntity)) {
			PatPatClientManager.removePatEntity(patEntity);
			return originalHeight;
		}


		return originalHeight * PatPatClientManager.getAnimationProgress(patEntity);
	}

}
