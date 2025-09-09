package net.lopymine.patpat.modmenu.pipec.image;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.GuiGraphics;

@Getter
@SuperBuilder
public class PatRenderer extends AbstractPatImage {

	private final RenderMethod method;

	@FunctionalInterface
	public interface RenderMethod {
		int render(GuiGraphics graphics, int x, int y, int renderWidth, float delta);
	}

}
