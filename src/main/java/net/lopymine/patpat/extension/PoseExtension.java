package net.lopymine.patpat.extension;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;

public class PoseExtension {

	public static Pose copy(Pose entry) {
		return entry.copy();
	}

}
