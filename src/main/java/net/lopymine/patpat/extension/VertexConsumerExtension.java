package net.lopymine.patpat.extension;

import /*? >=1.19.3 {*/org.joml.Matrix4f/*?} else {*/ /*com.mojang.math.Matrix4f*//*?}*/;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class VertexConsumerExtension {

	public static VertexConsumer withVertex(VertexConsumer consumer, Matrix4f matrix4f, float x, float y, float z) {
		//? if >=1.21 {
		return consumer.addVertex(matrix4f, x, y, z);
		//?} else {
		/*return consumer.vertex(matrix4f, x, y, z);
		*///?}
	}

	public static VertexConsumer withColor(VertexConsumer consumer, int r, int g, int b, int a) {
		//? if >=1.21 {
		return consumer.setColor(r, g, b, a);
		//?} else {
		/*return consumer.color(r, g, b, a);
		*///?}
	}

	public static VertexConsumer withOverlay(VertexConsumer consumer, int overlay) {
		//? if >=1.21 {
		return consumer.setOverlay(overlay);
		 //?} else {
		/*return consumer.overlayCoords(overlay);
		*///?}
	}

	public static VertexConsumer withUv(VertexConsumer consumer, float u, float v) {
		//? if >=1.21 {
		return consumer.setUv(u, v);
		//?} else {
		/*return consumer.uv(u, v);
		*///?}
	}

	public static VertexConsumer withNormal(VertexConsumer consumer, float x, float y, float z) {
		//? if >=1.21 {
		return consumer.setNormal(x, y, z);
		//?} else {
		/*return consumer.normal(x, y, z);
		*///?}
	}

	public static VertexConsumer withLight(VertexConsumer consumer, int light) {
		//? if >=1.21 {
		return consumer.setLight(light);
		//?} else {
		/*return consumer.uv2(light);
		*///?}
	}

	public static void end(VertexConsumer consumer) {
		/*? <=1.20.6 {*//*consumer.endVertex();*//*?}*/
	}

}
