package net.lopymine.patpat.extension;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;

public class PoseExtension {

	public static Pose copy(Pose entry) {
		//? if >=1.21 {
		return entry.copy();
		//?} elif >=1.17 && <=1.20.6 {
		/*PoseStack poseStack = new PoseStack();
		Pose copy = poseStack.last();
		//? if >=1.19.3 {
		/^copy.pose().set(entry.pose());
		copy.normal().set(entry.normal());
		^///?} elif >=1.17 {
		/^copy.pose().load(entry.pose());
		copy.normal().load(entry.normal());
		^///?}
		return copy;
		*///?} else {
		/*return new PoseStack.Pose(entry.pose(), entry.normal());
		*///?}
	}

}
