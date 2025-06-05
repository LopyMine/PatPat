package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
//? <=1.20.2 {
/*import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.client.manager.PatPatClientManager;

@Mixin(Camera.class)
public class CameraMixin {

	/*? >1.20.2 {*/
	@Shadow
	private float partialTickTime;
	//?} else {
	/*private float partialTickTime = 0;

	@Inject(at = @At("HEAD"), method = "update")
	private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		this.lastTickDelta = tickDelta;
	}
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

		if (PatPatClientManager.expired(patEntity, this.partialTickTime)) {
			PatPatClientManager.removePatEntity(patEntity);
			return originalHeight;
		}

		return originalHeight * PatPatClientManager.getAnimationProgress(patEntity, this.partialTickTime);
	}

}
