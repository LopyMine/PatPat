package net.lopymine.patpat.modmenu.common.image;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import net.minecraft.client.gui.GuiGraphicsExtractor;

@Getter
@SuperBuilder
public class PatRenderer extends AbstractPatImage {

	private final RenderMethod method;

	@FunctionalInterface
	public interface RenderMethod {
		int render(GuiGraphicsExtractor graphics, int x, int y, int renderWidth, float delta);
	}

}
