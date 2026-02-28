package net.lopymine.patpat.modmenu.common.image;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.resources.Identifier;

@Getter
@SuperBuilder
public class PatImage extends AbstractPatImage{

	private final Identifier resource;

}
