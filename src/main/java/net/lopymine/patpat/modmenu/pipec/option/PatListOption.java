package net.lopymine.patpat.modmenu.pipec.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class PatListOption<T> extends PatOption<List<T>> {

	private final int minEntries;
	private final int maxEntries;
	private final T initial;

	public Class<?> getType() {
		return initial.getClass();
	}

}
