package net.lopymine.patpat.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.render.feature.PatFeatureRenderer.Submit;
import net.lopymine.patpat.extension.*;
import net.lopymine.patpat.mixin.SubmitNodeCollectionAccessor;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.feature.*;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

@ExtensionMethod(value = {VertexConsumerExtension.class, PoseExtension.class})
public class PatFeatureRenderer extends RenderTypeFeatureRenderer<Submit> {

	public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create(PatPat.MOD_NAME);

	public static void submit(SubmitNodeCollector collector, Submit request) {
		if (!(collector instanceof SubmitNodeStorage storage)) {
			return;
		}
		SubmitNodeCollection collection = storage.order(0);
		if (!(collection instanceof SubmitNodeCollectionAccessor accessor)) {
			return;
		}
		accessor.getTranslucentModels().submit(request);
	}

	@Override
	protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
		for (Submit request : submits) {
			VertexConsumer buffer = this.getVertexBuilder(RenderTypes.entityTranslucent(request.texture()));

			org.joml.Matrix4f matrix = request.poseStack().pose();
			buffer.withVertex(matrix, request.x1(), request.y1(), request.z()).withColor(255, 255, 255, 255).withUv(request.u1(), request.v1()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
			buffer.withVertex(matrix, request.x1(), request.y2(), request.z()).withColor(255, 255, 255, 255).withUv(request.u1(), request.v2()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
			buffer.withVertex(matrix, request.x2(), request.y2(), request.z()).withColor(255, 255, 255, 255).withUv(request.u2(), request.v2()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
			buffer.withVertex(matrix, request.x2(), request.y1(), request.z()).withColor(255, 255, 255, 255).withUv(request.u2(), request.v1()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
		}
	}

	public record Submit (
			Identifier texture,
			Pose poseStack,
			float x1,
			float y1,
			float x2,
			float y2,
			float z,
			float u1,
			float v1,
			float u2,
			float v2,
			int light
	) implements TranslucentSubmit {

		@Override
		public float distanceToCameraSq() {
			return TranslucentSubmit.computeDistanceToCameraSq(this.poseStack.pose());
		}

		@Override
		public FeatureRendererType<? extends TranslucentSubmit> featureType() {
			return PatFeatureRenderer.TYPE;
		}

	}

}
