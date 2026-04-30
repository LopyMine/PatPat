package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.client.manager.PatPatClientManager;

@Mixin(Camera.class)
public abstract class CameraMixin {

	/*? >1.20.2 && <=1.21.11 {*/
	@Shadow
	private float partialTickTime;
	//?} elif <=1.21.11 {
	/*@Unique
	private float partialTickTime = 0;

	@Inject(at = @At("HEAD"), method = "setup")
	private void onUpdate(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
		this.partialTickTime = tickDelta;
	}
	*///?} else {
	/*@Shadow public abstract float getCameraEntityPartialTicks(DeltaTracker deltaTracker);
	*///?}

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

		//? if >=26.1 {
		/*float partialTickTime = this.getCameraEntityPartialTicks(Minecraft.getInstance().getDeltaTracker());
		*///?}

		if (PatPatClientManager.expired(patEntity, partialTickTime)) {
			PatPatClientManager.removePatEntity(patEntity);
			return originalHeight;
		}

		return originalHeight * PatPatClientManager.getAnimationProgress(patEntity, partialTickTime);
	}

}
