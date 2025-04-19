package net.lopymine.patpat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
//? <=1.20.2 {
/*import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.entity.PatEntity;
import net.lopymine.patpat.manager.client.PatPatClientManager;

@Mixin(Camera.class)
public class CameraMixin {

	//? >1.21.4 {
	/*@Shadow
	private float lastTickProgress;
	*//*?} else if >1.20.2 {*/
	@Shadow
	private float lastTickDelta;
	//?} else {
	/*private float lastTickDelta = 0;

	@Inject(at = @At("HEAD"), method = "update")
	private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		this.lastTickDelta = tickDelta;
	}
	*///?}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getStandingEyeHeight()F"), method = "updateEyeHeight")
	private float applyPattingEffect(Entity entity, Operation<Float> original) {
		//? >1.21.4
		/*float lastTickDelta = lastTickProgress;*/

		Float originalHeight = original.call(entity);

		if (!PatPatClient.getConfig().getVisualConfig().isCameraShackingEnabled()) {
			return originalHeight;
		}

		if (!(entity instanceof LivingEntity livingEntity)) {
			return originalHeight;
		}
		PatEntity patEntity = PatPatClientManager.getPatEntity(livingEntity);
		if (patEntity == null) {
			return originalHeight;
		}

		if (PatPatClientManager.expired(patEntity, this.lastTickDelta)) {
			PatPatClientManager.removePatEntity(patEntity);
			return originalHeight;
		}

		return originalHeight * PatPatClientManager.getAnimationProgress(patEntity, this.lastTickDelta);
	}

}
