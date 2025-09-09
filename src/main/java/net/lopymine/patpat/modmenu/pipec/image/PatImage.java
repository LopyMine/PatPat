package net.lopymine.patpat.modmenu.pipec.image;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.resources.ResourceLocation;

@Getter
@SuperBuilder
public class PatImage extends AbstractPatImage{

	private final ResourceLocation resource;

}
