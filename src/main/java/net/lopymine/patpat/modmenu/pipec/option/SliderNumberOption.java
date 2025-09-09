package net.lopymine.patpat.modmenu.pipec.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SliderNumberOption<T extends Number> extends NumberOption<T> {

	private final T step;

}
