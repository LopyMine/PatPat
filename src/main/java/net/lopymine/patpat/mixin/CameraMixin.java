package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.manager.PatPatClientManager;
import net.lopymine.patpat.entity.PatEntity;
import net.minecraft.client.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow public abstract float getCameraEntityPartialTicks(DeltaTracker deltaTracker);

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F"), method = "tick")
	private float applyPattingEffect(Entity entity, Operation<Float> original) {

		Float originalHeight = original.call(entity);

		if (!PatPatClientConfig.getInstance().getVisualConfig().isCameraShackingEnabled()) {
			return originalHeight;
		}

		if (!(entity instanceof LivingEntity livingEntity)) {
			return originalHeight;
		}
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return originalHeight;
		}

		float partialTickTime = this.getCameraEntityPartialTicks(Minecraft.getInstance().getDeltaTracker());

		if (PatPatClientManager.expired(patEntity, partialTickTime)) {
			PatPatClientManager.removePatEntity(patEntity);
			return originalHeight;
		}

		return originalHeight * PatPatClientManager.getAnimationProgress(patEntity, partialTickTime);
	}

}
