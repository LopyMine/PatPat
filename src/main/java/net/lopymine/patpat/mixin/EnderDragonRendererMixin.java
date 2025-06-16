package net.lopymine.patpat.mixin;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;

@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
	private void makeDragonPattableBecauseWhyNot(EnderDragonRenderState enderDragonRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) enderDragonRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) enderDragonRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, poseStack, tickDelta);
	}

}
