package net.lopymine.patpat.client.render.feature;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.*;
import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.extension.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@ExtensionMethod(value = {VertexConsumerExtension.class, PoseExtension.class})
public class PatFeatureRenderer {

	private final List<PatFeatureRequest> requests = new ArrayList<>();

	private static final PatFeatureRenderer INSTANCE = new PatFeatureRenderer();

	public static PatFeatureRenderer getInstance() {
		return INSTANCE;
	}

	public void render() {
		BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

		for (PatFeatureRequest request : this.requests) {
			VertexConsumer buffer = source.getBuffer(RenderType.entityTranslucent(request.texture()));

			/*? if >=1.19.3 {*/ org.joml.Matrix4f /*?} else {*/ /*com.mojang.math.Matrix4f*//*?}*/ matrix = request.poseStack().pose();
			buffer.withVertex(matrix, request.x1(), request.y1(), request.z()).withColor(255, 255, 255, 255).withUv(request.u1(), request.v1()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
			buffer.withVertex(matrix, request.x1(), request.y2(), request.z()).withColor(255, 255, 255, 255).withUv(request.u1(), request.v2()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
			buffer.withVertex(matrix, request.x2(), request.y2(), request.z()).withColor(255, 255, 255, 255).withUv(request.u2(), request.v2()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();
			buffer.withVertex(matrix, request.x2(), request.y1(), request.z()).withColor(255, 255, 255, 255).withUv(request.u2(), request.v1()).withOverlay(OverlayTexture.NO_OVERLAY).withLight(request.light()).withNormal(0, 1, 0).end();

		}

		this.requests.clear();
	}

	public void request(
			ResourceLocation texture,
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
	) {
		this.requests.add(new PatFeatureRequest(texture, poseStack.copy(), x1, y1, x2, y2, z, u1, v1, u2, v2, light));
	}

}