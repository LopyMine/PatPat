package net.lopymine.patpat.modmenu.common.image;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

//? >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}

@Getter
@SuperBuilder
public class PatRenderer extends AbstractPatImage {

	private final RenderMethod method;

	@FunctionalInterface
	public interface RenderMethod {
		int render(/*? >=1.20 {*/GuiGraphics graphics/*?} else {*//*PoseStack poseStack*//*?}*/, int x, int y, int renderWidth, float delta);
	}

}
