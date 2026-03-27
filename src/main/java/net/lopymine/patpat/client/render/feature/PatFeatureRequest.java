package net.lopymine.patpat.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public record PatFeatureRequest(
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
        int light,
		@Nullable MultiBufferSource provider
) { }
