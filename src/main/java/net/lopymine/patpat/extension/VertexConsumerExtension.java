package net.lopymine.patpat.extension;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class VertexConsumerExtension {

	public static VertexConsumer withVertex(VertexConsumer consumer, Matrix4f matrix4f, float x, float y, float z) {
		return consumer.addVertex(matrix4f, x, y, z);
	}

	public static VertexConsumer withColor(VertexConsumer consumer, int r, int g, int b, int a) {
		return consumer.setColor(r, g, b, a);
	}

	public static VertexConsumer withOverlay(VertexConsumer consumer, int overlay) {
		return consumer.setOverlay(overlay);
	}

	public static VertexConsumer withUv(VertexConsumer consumer, float u, float v) {
		return consumer.setUv(u, v);
	}

	public static VertexConsumer withNormal(VertexConsumer consumer, float x, float y, float z) {
		return consumer.setNormal(x, y, z);
	}

	public static VertexConsumer withLight(VertexConsumer consumer, int light) {
		return consumer.setLight(light);
	}

	public static void end(VertexConsumer consumer) {
	}

}
