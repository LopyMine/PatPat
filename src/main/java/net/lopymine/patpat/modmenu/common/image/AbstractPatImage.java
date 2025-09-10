package net.lopymine.patpat.modmenu.common.image;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class AbstractPatImage {

	private final ImageType type;
	private int width;
	private int height;

}
