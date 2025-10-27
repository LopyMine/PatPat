package net.lopymine.patpat.modmenu.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.lopymine.patpat.modmenu.common.image.AbstractPatImage;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

@Getter
@Builder
public class PatDescription {
	private final Component text;
	@Setter
	private @Nullable AbstractPatImage image;

	public static PatDescription of(Component text){
		return new PatDescription(text, null);
	}

}
