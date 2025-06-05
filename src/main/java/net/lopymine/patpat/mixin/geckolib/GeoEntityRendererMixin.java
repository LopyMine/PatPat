package net.lopymine.patpat.mixin.geckolib;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin {

	@Inject(at=@At(value = "HEAD"), method = "render")
	private void render(EntityRenderState entityRenderState, PoseStack matrixStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
		Entity entity = ((EntityRenderStateWithParent) entityRenderState).patPat$getEntity();
		float tickDelta = ((EntityRenderStateWithParent) entityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, matrixStack, tickDelta);
	}
}

