package net.lopymine.patpat.client.render;

import net.minecraft.client.render.*;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.*;

import java.util.function.Function;

public class PatPatClientRenderLayers {

	private static final Function<Identifier, RenderLayer> HAND_RENDER_LAYER = Util.memoize((texture -> {
		MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
				.program(RenderLayer.ENTITY_TRANSLUCENT_PROGRAM)
				.texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
				.transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
				.cull(RenderLayer.DISABLE_CULLING)
				.lightmap(RenderLayer.ENABLE_LIGHTMAP)
				.overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
				.depthTest(RenderPhase.BIGGER_DEPTH_TEST)
				.build(true);

		return RenderLayer.of("hand_render_layer", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
	}));

	public static RenderLayer getHandRenderLayer(Identifier texture) {
		return HAND_RENDER_LAYER.apply(texture);
	}

}
