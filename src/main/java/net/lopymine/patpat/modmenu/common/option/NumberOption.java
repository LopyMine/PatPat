package net.lopymine.patpat.modmenu.common.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NumberOption<T extends Number> extends PatOption<T> {

    private final T min;
    private final T max;

}
