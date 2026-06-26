package net.lopymine.patpat.mixin.geckolib;

//? if geckolib {

import com.geckolib.renderer.GeoEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin {

	@Inject(at = @At(value = "HEAD"), method = "submit")
	private void render(EntityRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState, CallbackInfo ci) {
		Entity entity = ((net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent) entityRenderState).patPat$getEntity();
		float partialTick = ((net.lopymine.patpat.utils.mixin.EntityRenderStateWithParent) entityRenderState).patPat$getTickDelta();
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}
		PatPatClientRenderer.scaleEntityIfPatted(livingEntity, poseStack, partialTick);
	}

}
//?}

