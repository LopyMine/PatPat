package net.lopymine.patpat.modmenu.pipec.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@SuperBuilder
public abstract class PatOption<T> extends AbstractPatOption<T> {

	private final T defaultValue;
	private final Supplier<T> getter;
	private final Consumer<T> setter;

	public Class<?> getType() {
		return defaultValue.getClass();
	}

}
