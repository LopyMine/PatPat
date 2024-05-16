package net.lopymine.patpat.mixin;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.AnimationConfig;
import net.lopymine.patpat.entity.PatEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		PatEntity patEntity = PatPatClient.getPatEntity(livingEntity);
		if (patEntity == null) {
			return;
		}

		AnimationConfig animationConfig = patEntity.getAnimation();
		int duration = animationConfig.getDuration();
		long time = patEntity.getTimeOfStart();
		long timeNow = System.currentTimeMillis();
		if (timeNow >= (time + duration)) {
			PatPatClient.removePatEntity(patEntity);
			return;
		}

		float animationProgress = MathHelper.clamp((float) (timeNow - time) / duration, 0.0F, 1.0F);
		animationProgress = (float) (1 - Math.pow(1 - animationProgress, 2));
		int totalFrames = animationConfig.getTotalFrames();

		int frame = MathHelper.clamp((int) Math.floor(totalFrames * animationProgress), 0, totalFrames - 1);
		patEntity.setFrame(frame);

		float range = 0.425F / livingEntity.getHeight();
		float animation = ((float) ((1 - range) + range * (1 - Math.sin(animationProgress * Math.PI))));

		matrixStack.scale(1, animation, 1);
	}
}
