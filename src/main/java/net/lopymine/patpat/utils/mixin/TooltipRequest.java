package net.lopymine.patpat.utils.mixin;

//? if <1.19.3 {

import com.mojang.blaze3d.vertex.PoseStack;

public interface TooltipRequest {

	void render(PoseStack stack, int mouseX, int mouseY, float partialTick);

}
//?}
